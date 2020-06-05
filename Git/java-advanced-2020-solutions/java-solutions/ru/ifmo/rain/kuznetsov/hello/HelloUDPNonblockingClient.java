package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
                datagramChannel.register(selector, SelectionKey.OP_WRITE, threadId);
            } catch (IOException e) {
                log("ERROR: " + e.getMessage());
                return;
            }
        }
        while (selector.isOpen()) {
            if (selector.selectedKeys().size() == 0) {
                try {
                    selector.select();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            for (final Iterator <SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
                final SelectionKey key = it.next();
                if (key.isWritable()) {
                    for (int i = 0; i < requestCount; i++) {
                        String answer = prefix + key.attachment() + "_" + i;
                        DatagramChannel senderChannel = (DatagramChannel) key.channel();
                        ByteBuffer request = ByteBuffer.allocate(2048);
                        request.clear();
                        request.put(answer.getBytes());
                        request.flip();
                        //System.out.println("Sending: " + answer);
                        try {
                            senderChannel.send(request, address);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
                if (key.isReadable()) {
                    System.out.println("Reading");
                    ByteBuffer receive = ByteBuffer.allocate(2048);
                    receive.clear();
                    DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                    try {
                        datagramChannel.receive(receive);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    String responseString = new String(receive.array(), StandardCharsets.UTF_8);
                    if (responseString.matches("[\\D]*" + key.attachment() + "[\\D]*" + "[\\D]*")) {
                        System.out.println("New answer:\n\tRequest: " + responseString + "\n\tResponse: " + responseString);
                        break;
                    } else {
                        System.out.println("New wrong answer:\n\tRequest: " + responseString + "\n\tResponse: " + responseString);
                        break;
                    }
                }
                it.remove();
            }
        }
    }

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

    private void log(String s) {
        System.out.println(s);
    }
}
