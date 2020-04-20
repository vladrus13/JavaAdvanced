package info.kgeorgiy.java.advanced.hello;

import info.kgeorgiy.java.advanced.base.BaseTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import static info.kgeorgiy.java.advanced.hello.Util.response;

/**
 * Full tests for {@link HelloServer}.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloServerTest extends BaseTest {
    private static final AtomicInteger port = new AtomicInteger(28800);
    public static final String REQUEST = HelloServerTest.class.getName();

    @Test
    public void test01_singleRequest() throws IOException {
        test(1, port -> socket -> checkResponse(port, socket, REQUEST));
    }

    @Test
    public void test02_multipleClients() throws IOException {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, 1);
            for (int i = 0; i < 10; i++) {
                client(port, REQUEST + i);
            }
        }
    }

    @Test
    public void test03_multipleRequests() throws IOException {
        test(1, port -> socket -> {
            for (int i = 0; i < 10; i++) {
                checkResponse(port, socket, REQUEST + i);
            }
        });
    }

    @Test
    public void test04_parallelRequests() throws IOException {
        test(1, port -> socket -> {
            final Set<String> responses = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                final String request = REQUEST + i;
                responses.add(response(request));
                send(port, socket, request);
            }
            for (int i = 0; i < 10; i++) {
                final String response = Util.receive(socket);
                Assert.assertTrue("Unexpected response " + response, responses.remove(response));
            }
        });
    }

    @Test
    public void test05_parallelClients() {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, 1);
            parallel(10, () -> client(port, REQUEST));
        }
    }

    @Test
    public void test06_dos() throws IOException {
        test(1, port -> socket -> parallel(100, () -> {
            for (int i = 0; i < 10000; i++) {
                send(port, socket, REQUEST);
            }
        }));
    }

    @Test
    public void test07_noDoS() {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, 10);
            parallel(10, () -> {
                try (final DatagramSocket socket = new DatagramSocket(null)) {
                    for (int i = 0; i < 10000; i++) {
                        checkResponse(port, socket, REQUEST + i);
                    }
                }
            });
        }
    }

    private static void send(final int port, final DatagramSocket socket, final String request) throws IOException {
        Util.send(socket, request, new InetSocketAddress("localhost", port));
    }

    private static void client(final int port, final String request) throws IOException {
        try (final DatagramSocket socket = new DatagramSocket(null)) {
            checkResponse(port, socket, request);
        }
    }

    public static void test(final int workers, final IntFunction<ConsumerCommand<DatagramSocket, IOException>> command) throws IOException {
        try (final HelloServer server = createCUT()) {
            final int port = getPort();
            server.start(port, workers);
            try (final DatagramSocket socket = new DatagramSocket()) {
                command.apply(port).run(socket);
            }
        }
    }

    private static void checkResponse(final int port, final DatagramSocket socket, final String request) throws IOException {
        final String response = Util.request(request, socket, new InetSocketAddress("localhost", port));
        Assert.assertEquals("Invalid response", response(request), response);
    }

    private static int getPort() {
        return port.getAndIncrement();
    }
}
