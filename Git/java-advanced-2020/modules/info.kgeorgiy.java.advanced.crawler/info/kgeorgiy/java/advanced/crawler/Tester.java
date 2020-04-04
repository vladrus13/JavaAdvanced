package info.kgeorgiy.java.advanced.crawler;

import info.kgeorgiy.java.advanced.base.BaseTester;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Tester extends BaseTester {
    public static void main(final String... args) {
        new Tester()
                .add("easy", CrawlerEasyTest.class)
                .add("hard", CrawlerHardTest.class)
                .run(args);
    }

    static {
        assert Downloader.class.isAssignableFrom(CachingDownloader.class);
    }
}
