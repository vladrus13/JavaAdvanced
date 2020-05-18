package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPServer implements HelloServer {
    private DatagramSocket socket;
    private ExecutorService workers;

    @Override
    public void start(int port, int threadCount) {
        try {
            socket = new DatagramSocket(port);
            int packageSize = socket.getSendBufferSize();
            workers = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                workers.submit(() -> {
                    while (!socket.isClosed()) {
                        DatagramPacket packet = new DatagramPacket(new byte[packageSize], packageSize);
                        try {
                            socket.receive(packet);
                            packet.setData(("Hello, " + new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
                            socket.send(packet);
                        } catch (IOException ignored) {
                        }
                    }
                });
            }
        } catch (SocketException e) {
            // bad socket
        }
    }

    @Override
    public void close() {
        socket.close();
        workers.shutdown();
        try {
            workers.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {

        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.out.println("2 args must be");
            return;
        }
        try {
            try (HelloServer server = new HelloUDPServer()) {
                server.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (NumberFormatException exception) {
            System.out.println("Error on parse" + exception.getMessage());
        }
    }
}
