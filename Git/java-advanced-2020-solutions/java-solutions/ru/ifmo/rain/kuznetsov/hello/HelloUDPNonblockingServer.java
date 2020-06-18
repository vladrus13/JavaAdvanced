package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Impl for HelloServer class
 */
public class HelloUDPNonblockingServer implements HelloServer {

    /**
     * Queue for used package
     */
    private final ConcurrentLinkedQueue<Package> used = new ConcurrentLinkedQueue<>();

    /**
     * Buffer size for channels
     */
    private int BUFFER_SIZE;

    /**
     * Queue for free packages. We can took that
     */
    private ConcurrentLinkedDeque<Package> free;

    /**
     * Channel from which we get bytes
     */
    private DatagramChannel channel;

    /**
     * Selector for channel
     */
    private Selector selector;

    /**
     * Worker for threads
     */
    private ExecutorService workers;

    /**
     * Is server closed
     */
    private boolean closed = false;

    @Override
    public void start(int port, int threadCount) {
        free = new ConcurrentLinkedDeque<>();
        try {
            selector = Selector.open();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            log("ERROR: " + e.getMessage());
            return;
        }

        BUFFER_SIZE = 2048; //channel.socket().getReceiveBufferSize();

        IntStream.range(0, threadCount).forEach(i -> {
            free.add(new Package(ByteBuffer.allocate(BUFFER_SIZE), null));
        });

        workers = Executors.newFixedThreadPool(threadCount + 1);
        workers.submit(() -> {
            while (!Thread.interrupted() && !channel.socket().isClosed() && !closed) {
                try {
                    selector.select(200);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                for (final Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext() && !closed; ) {
                    final SelectionKey key = it.next();
                    if (key.isReadable()) {
                        try {
                            Package freePackage = free.pop();
                            ByteBuffer byteBuffer = freePackage.getRequest().clear();
                            if (free.isEmpty()) {
                                key.interestOpsAnd(~SelectionKey.OP_READ);
                                selector.wakeup();
                            }
                            DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                            SocketAddress socketAddress;
                            socketAddress = datagramChannel.receive(byteBuffer);
                            SocketAddress finalSocketAddress = socketAddress;
                            // log("Get: " + new String(byteBuffer.array(), StandardCharsets.UTF_8) + " from " + socketAddress.toString());
                            workers.submit(() -> {
                                byteBuffer.flip();
                                String request = "Hello, " + StandardCharsets.UTF_8.decode(byteBuffer);
                                // log("Attach: " + request + " from " + finalSocketAddress.toString());
                                freePackage.setRequest(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8)));
                                freePackage.setSocketAddress(finalSocketAddress);
                                used.add(freePackage);
                                key.interestOpsOr(SelectionKey.OP_WRITE);
                                selector.wakeup();
                            });
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        if (key.isWritable()) {
                            Package attachmentPackage = used.remove();
                            if (used.isEmpty()) {
                                key.interestOpsAnd(~SelectionKey.OP_WRITE);
                                selector.wakeup();
                            }
                            ByteBuffer byteBuffer = attachmentPackage.getRequest();
                            DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                            // log("Send: " + new String(attachmentPackage.getRequest().array(), StandardCharsets.UTF_8) + " to " + attachmentPackage.getSocketAddress());
                            try {
                                datagramChannel.send(byteBuffer, attachmentPackage.getSocketAddress());
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            byteBuffer.clear().flip();
                            free.add(attachmentPackage);
                            key.interestOpsOr(SelectionKey.OP_READ);
                            selector.wakeup();
                        }
                    }
                    it.remove();
                }
            }
        });
    }

    @Override
    public void close() {
        closed = true;
        workers.shutdown();
        try {
            selector.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            channel.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            workers.awaitTermination(500, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Main for Impl HelloServer
     *
     * @param args arguments, like <port>, <thread count>
     */
    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.out.println("2 args must be");
            return;
        }
        try {
            try (HelloUDPNonblockingServer server = new HelloUDPNonblockingServer()) {
                server.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (NumberFormatException exception) {
            System.out.println("Error on parse" + exception.getMessage());
        }
    }

    /**
     * Write string to console
     *
     * @param s which string we write
     */
    private void log(String s) {
        System.out.println(s);
    }
}
