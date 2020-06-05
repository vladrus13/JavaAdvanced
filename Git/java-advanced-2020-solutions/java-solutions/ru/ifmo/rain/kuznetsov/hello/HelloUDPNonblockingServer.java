package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;

public class HelloUDPNonblockingServer implements HelloServer {

    private Selector selector;

    @Override
    public void start(int port, int threadCount) {
        try {
            selector = Selector.open();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        DatagramChannel datagramChannel;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            datagramChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        } catch (IOException exception) {
            exception.printStackTrace();
            log("ERROR: " + exception.getMessage());
            return;
        }
        ByteBuffer receive = ByteBuffer.allocate(2048);
        ByteBuffer send = ByteBuffer.allocate(2048);
        for (int threadNum = 0; threadNum < threadCount; threadNum++) {
            try {
                datagramChannel.register(selector, SelectionKey.OP_READ);
                receive.clear();
                datagramChannel.read(receive);
                send.clear();
                send.put("Hello, ".getBytes(StandardCharsets.UTF_8));
                send.flip();
                datagramChannel.send(send, new InetSocketAddress(InetAddress.getLocalHost(), port));
            } catch (IOException e) {
                log("ERROR: " + e.getMessage());
                return;
            }
        }
    }

    @Override
    public void close() {

    }

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

    private void log(String s) {
        System.out.println(s);
    }
}
