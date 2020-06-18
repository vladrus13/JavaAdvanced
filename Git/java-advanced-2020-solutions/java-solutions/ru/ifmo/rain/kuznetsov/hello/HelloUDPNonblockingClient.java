package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Impl for HelloClient class
 */
public class HelloUDPNonblockingClient implements HelloClient {
    @Override
    public void run(String hostName, int port, String prefix, int threadCount, int requestCount) {
        Selector selector;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            log("ERROR: Can't open selector: " + e.getMessage());
            return;
        }

        SocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(hostName), port);
        } catch (UnknownHostException ignored) {
            log("ERROR: Bad HostName");
            return;
        }

        for (int threadId = 0; threadId < threadCount; threadId++) {
            try {
                DatagramChannel datagramChannel = DatagramChannel.open();
                datagramChannel.configureBlocking(false);
                datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                datagramChannel.connect(address);
                datagramChannel.register(selector, SelectionKey.OP_WRITE, new int[]{threadId, 0});
            } catch (IOException e) {
                log("ERROR: " + e.getMessage());
                return;
            }
        }
        while (!Thread.interrupted() && !selector.keys().isEmpty()) {
            try {
                selector.select(200);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            if (selector.selectedKeys().isEmpty()) {
                for (final SelectionKey key : selector.keys()) {
                    send(prefix, address, key);
                }
            } else {
                for (final Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
                    final SelectionKey key = it.next();
                    if (key.isWritable()) {
                        key.interestOps(SelectionKey.OP_WRITE);
                        send(prefix, address, key);
                    } else {
                        if (key.isReadable()) {
                            // read
                            ByteBuffer receive = ByteBuffer.allocate(2048);
                            receive.clear();
                            DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                            try {
                                datagramChannel.receive(receive);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                            String responseString = new String(receive.array(), StandardCharsets.UTF_8);
                            int[] attachment = (int[]) key.attachment();
                            if (responseString.matches("[\\D]*" + attachment[0] + "[\\D]*" + attachment[1] + "[\\D]*")) {
                                if (attachment[1] < requestCount - 1) {
                                    key.attach(new int[]{attachment[0], attachment[1] + 1});
                                    key.interestOps(SelectionKey.OP_WRITE);
                                } else {
                                    try {
                                        key.channel().close();
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
                                }
                                // System.out.println("New answer:\n\tReading: " + responseString);
                                break;
                            } else {
                                send(prefix, address, key);
                                // System.out.println("New wrong answer:\n\tRequest: " + responseString + "\n\tRegex: " + "[!digits]" + attachment[0] + "[!digits]" + attachment[1]);
                            }

                        }
                    }
                    it.remove();
                }
            }
        }
    }

    /**
     * Send package
     * @param prefix prefix for message
     * @param address address there we send
     * @param key {@link SelectionKey} key port
     */
    private void send(String prefix, SocketAddress address, SelectionKey key) {
        int[] attachment = (int[]) key.attachment();
        String answer = prefix + attachment[0] + "_" + attachment[1];
        DatagramChannel senderChannel = (DatagramChannel) key.channel();
        ByteBuffer request = ByteBuffer.allocate(2048);
        request.clear();
        request.put(answer.getBytes());
        request.flip();
        // System.out.println("Sending: " + answer);
        try {
            senderChannel.send(request, address);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    /**
     * Main for impl HelloClient
     * @param args arguments, like <host name>, <port>, <prefix>, <thread count>, <request count>
     */
    public static void main(String[] args) {
        if (args == null || args.length != 5 || args[0] == null || args[1] == null || args[2] == null || args[3] == null || args[4] == null) {
            System.out.println("Wrong input");
            return;
        }
        try {
            new HelloUDPClient().run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } catch (NumberFormatException exception) {
            System.out.println("Wrong format numbers" + exception.getMessage());
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
