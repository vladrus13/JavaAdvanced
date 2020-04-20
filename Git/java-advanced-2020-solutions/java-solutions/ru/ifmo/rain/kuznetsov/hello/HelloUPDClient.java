package ru.ifmo.rain.kuznetsov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUPDClient implements HelloClient {
    @Override
    public void run(String hostName, int port, String prefix, int threadCount, int requestCount) {
        SocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(hostName), port);
        } catch (UnknownHostException ignored) {
            // bad hostname
            return;
        }
        ExecutorService workers = Executors.newFixedThreadPool(threadCount);
        for (int threadId = 0; threadId < threadCount; threadId++) {
            int finalThreadId = threadId;
            workers.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    socket.setSoTimeout(1000);
                    DatagramPacket response = new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());
                    DatagramPacket request = new DatagramPacket(new byte[0], 0, address);
                    for (int requestId = 0; requestId < requestCount; requestId++) {
                        String requestString = prefix + finalThreadId + "_" + requestId;
                        request.setData(requestString.getBytes(StandardCharsets.UTF_8));
                        try {
                            while (!socket.isClosed()) {
                                socket.send(request);
                                socket.receive(response);
                                System.out.println("New answer:\n\tRequest: " + requestString + "\n\tResponse: " + new String(response.getData(), response.getOffset(), response.getLength(), StandardCharsets.UTF_8));
                            }
                        } catch (IOException ignored) {
                            System.out.println("New answer:\n\tFailed on " + requestString);
                        }
                    }
                } catch (SocketException e) {
                    // bad creating socket
                }
            });
        }
        workers.shutdown();
    }
}
