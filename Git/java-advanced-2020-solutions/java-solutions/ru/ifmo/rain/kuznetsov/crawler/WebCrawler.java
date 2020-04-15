package ru.ifmo.rain.kuznetsov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

public class WebCrawler implements Crawler {
    /**
     * {@link Downloader} for download page
     */
    private final Downloader downloader;
    /**
     * How much downloads at moment can be
     */
    private final int perHost;
    /**
     * Downloaders {@link ExecutorService}
     */
    private final ExecutorService downloaderPool;
    /**
     * Extractors {@link ExecutorService}
     */
    private final ExecutorService extractorPool;
    /**
     * Hosts map
     */
    private final Map<String, Host> hostMap;

    /**
     * Constuctor for {@link WebCrawler}
     * @param downloader downloader {@link Downloader}
     * @param downloaders count of downloaders
     * @param extractors count of extractors
     * @param perHost count of downloads of host
     */
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.perHost = perHost;
        downloaderPool = Executors.newFixedThreadPool(downloaders);
        extractorPool = Executors.newFixedThreadPool(extractors);
        hostMap = new ConcurrentHashMap<>();
    }

    /**
     * Download all necessary links
     * @param url start url
     * @param depth how deep it is allowed to go
     * @param downloaded downloaded links
     * @param errors errors on download links
     * @param viewed viewed links
     * @param phaser phaser
     */
    @SuppressWarnings("ConstantConditions")
    private void addBreadthFirstSearch(String url, int depth, Set<String> downloaded, Map<String, IOException> errors, Set<String> viewed, Phaser phaser) {
        Queue<Pair> urls = new ConcurrentLinkedDeque<>();
        urls.add(new Pair(url, 1));
        int currentWave = 0;
        while (!urls.isEmpty()) {
            if (urls.peek().getDepth() > currentWave) {
                currentWave++;
                phaser.arriveAndAwaitAdvance();
                continue;
            }
            Pair currentUrl = urls.poll();
            try {
                String host = URLUtils.getHost(currentUrl.getUrl());
                Host data = hostMap.computeIfAbsent(host, element -> new Host(perHost, downloaderPool));
                phaser.register();
                data.add(() -> {
                    try {
                        Document document = downloader.download(currentUrl.getUrl());
                        downloaded.add(currentUrl.getUrl());
                        if (currentUrl.getDepth() < depth) {
                            phaser.register();
                            extractorPool.submit(() -> {
                                try {
                                    document.extractLinks().stream().filter(element -> !viewed.contains(element)).filter(viewed::add).forEach(element -> urls.add(new Pair(element, currentUrl.getDepth() + 1)));
                                } catch (IOException e) {
                                    // ignored
                                } finally {
                                    phaser.arriveAndDeregister();
                                }
                            });
                        }
                    } catch (IOException e) {
                        errors.put(currentUrl.getUrl(), e);
                    } finally {
                        phaser.arriveAndDeregister();
                        data.next();
                    }
                });
            } catch (MalformedURLException e) {
                errors.put(currentUrl.getUrl(), e);
            }
            if (urls.isEmpty()) {
                phaser.arriveAndAwaitAdvance();
            }
        }
    }

    @Override
    public Result download(String url, int depth) {
        Set<String> downloaded = ConcurrentHashMap.newKeySet();
        Map<String, IOException> errors = new ConcurrentHashMap<>();
        Set<String> viewed = ConcurrentHashMap.newKeySet();
        Phaser phaser = new Phaser(1);
        viewed.add(url);
        addBreadthFirstSearch(url, depth, downloaded, errors, viewed, phaser);
        phaser.arriveAndAwaitAdvance();
        return new Result(new ArrayList<>(downloaded), errors);
    }

    @Override
    public void close() {
        downloaderPool.shutdown();
        extractorPool.shutdown();
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Must be 4 arg");
        }
        try {
            WebCrawler webCrawler = new WebCrawler(new CachingDownloader(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            webCrawler.download(args[0], Integer.parseInt(args[1]));
        } catch (IOException e) {
            System.out.println("Can't create CachingDownloader" + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Can't parse" + e.getMessage());
        }
    }

    /**
     * Pair for BFS-algorithm
     */
    private static class Pair {
        /**
         * URL
         */
        private final String url;
        /**
         * depth
         */
        private final int depth;

        /**
         * Constructor
         * @param url url)
         * @param depth depth
         */
        public Pair(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }

        /**
         * Getter for URL
         * @return URL
         */
        public String getUrl() {
            return url;
        }

        /**
         * Getter for depth
         * @return depth
         */
        public int getDepth() {
            return depth;
        }
    }
}
