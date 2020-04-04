package ru.ifmo.rain.kuznetsov.crawler;

import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;

public class WebCrawler implements Crawler {

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        
    }

    @Override
    public Result download(String s, int i) {
        return null;
    }

    @Override
    public void close() {

    }
}
