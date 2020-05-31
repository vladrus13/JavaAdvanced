package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPNonblockingClient implements HelloClient {

    @Override
    public void run(String hostName, int port, String prefix, int threadCount, int requestCount) {
        SocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(hostName), port);
        } catch (UnknownHostException ignored) {
            // "ERROR: Bad HostName"
            return;
        }
        ExecutorService workers = Executors.newFixedThreadPool(threadCount);
        for (int threadId = 0; threadId < threadCount; threadId++) {
            int finalThreadId = threadId;
            workers.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(100);
                    DatagramPacket response = new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());
                    DatagramPacket request = new DatagramPacket(new byte[0], 0, address);
                    for (int requestId = 0; requestId < requestCount; requestId++) {
                        String requestString = prefix + finalThreadId + "_" + requestId;
                        while (!socket.isClosed() && !Thread.interrupted()) {
                            try {
                                request.setData(requestString.getBytes(StandardCharsets.UTF_8));
                                socket.send(request);
                                socket.receive(response);
                                String responseString = new String(response.getData(), response.getOffset(), response.getLength(), StandardCharsets.UTF_8);
                                if (responseString.matches("[\\D]*" + finalThreadId + "[\\D]*" + requestId + "[\\D]*")) {
                                    System.out.println("New answer:\n\tRequest: " + requestString + "\n\tResponse: " + responseString);
                                    break;
                                }
                            } catch (IOException ignored) {
                                // "New answer:\n\tFailed on " + requestString + " Message: " + ignored.getMessage()
                            }
                        }
                    }
                } catch (SocketException e) {
                    // "ERROR: Bad socket"
                }
            });
        }
        workers.shutdown();
        try {
            workers.awaitTermination(10 * requestCount * threadCount, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 5 || args[0] == null || args[1] == null || args[2] == null || args[3] == null || args[4] == null) {
            System.out.println("Wrong input");
            return;
        }
        try {
            new HelloUDPNonblockingClient().run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } catch (NumberFormatException exception) {
            System.out.println("Wrong format numbers" + exception.getMessage());
        }
    }
}
