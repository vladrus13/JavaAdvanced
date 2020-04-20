package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUPDServer implements HelloServer {

    private int packageSize;
    private DatagramSocket socket;
    private ExecutorService workers;

    @Override
    public void start(int port, int threadCount) {
        try {
            socket = new DatagramSocket(port);
            packageSize = socket.getSendBufferSize();
            workers = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                workers.submit(() -> {
                    while (!socket.isClosed()) {
                        DatagramPacket packet = new DatagramPacket(new byte[packageSize], packageSize);
                        try {
                            socket.receive(packet);
                            packet.setData(("Hello, " + new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
                            socket.send(packet);
                            System.out.println("New answer\n");
                        } catch (IOException ignored) { }
                    }
                });
            }
        } catch (SocketException e) {
            // bad soket
        }
    }

    @Override
    public void close() {
        socket.close();
        workers.shutdown();
    }
}
