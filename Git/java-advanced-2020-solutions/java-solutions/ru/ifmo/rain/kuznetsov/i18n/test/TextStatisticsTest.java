package ru.ifmo.rain.kuznetsov.i18n.test;

import org.junit.Test;
import ru.ifmo.rain.kuznetsov.i18n.TextStatistics;

public class TextStatisticsTest {

    private final String prefix = "ru/ifmo/rain/kuznetsov/i18n/test/tests/";

    private void test(String fileName, String textLocale, String writeLocale) {
        System.out.println("===========================\nTest: " + fileName + "\n");
        TextStatistics.main(new String[]{textLocale, writeLocale, fileName + ".in",  fileName + ".html"});
    }

    @Test
    public void testArabic() {
        test(prefix + "ArabicTest", "ar", "en-US");
    }

    @Test
    public void testBigTest() {
        test(prefix + "BigTest", "en-US", "en-US");
    }

    @Test
    public void testChinese() {
        test(prefix + "ChineseTest", "ch", "en-US");
    }

    @Test
    public void testDate() {
        test(prefix + "DateTest", "en-US", "en-US");
    }

    @Test
    public void testEmpty() {
        test(prefix + "EmptyTest", "en-US", "en-US");
    }

    @Test
    public void testFirst() {
        test(prefix + "FirstTest", "en-US", "en-US");
    }

    @Test
    public void testJapanese() {
        test(prefix + "JapaneseTest", "ja", "en-US");
    }

    @Test
    public void testLorem() {
        test(prefix + "LoremTest", "en-US", "en-US");
    }

    @Test
    public void testOnlyNumbers() {
        test(prefix + "OnlyNumbersTest", "en-US", "en-US");
    }

    @Test
    public void testRussian() {
        test(prefix + "RussianTest", "ru", "ru-RU");
    }
}
