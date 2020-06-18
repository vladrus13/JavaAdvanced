package ru.ifmo.rain.kuznetsov.hello;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 * Package for send in NonBlocking
 */
public class Package {
    /**
     * {@link ByteBuffer} Request
     */
    private ByteBuffer request;
    /**
     * {@link SocketAddress} socket address
     */
    private SocketAddress socketAddress;

    /**
     * Constructor for Package
     * @param request request
     * @param socketAddress socket address
     */
    public Package(ByteBuffer request, SocketAddress socketAddress) {
        this.request = request;
        this.socketAddress = socketAddress;
    }

    /**
     * Getter for request
     * @return {@link ByteBuffer} request
     */
    public ByteBuffer getRequest() {
        return request;
    }

    /**
     * Getter for socket address
     * @return {@link SocketAddress} socket address
     */
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    /**
     * Setter for request
     * @param request {@link ByteBuffer} request
     */
    public void setRequest(ByteBuffer request) {
        this.request = request;
    }

    /**
     * Setter for socket address
     * @param socketAddress {@link SocketAddress} socket address
     */
    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
