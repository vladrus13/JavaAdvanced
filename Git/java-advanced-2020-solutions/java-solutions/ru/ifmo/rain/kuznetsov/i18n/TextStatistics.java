package ru.ifmo.rain.kuznetsov.i18n;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TextStatistics {

    private static NumberFormat NUMBER_INSTANCE;
    private static DateFormat DATE_FORMAT;
    private static NumberFormat CURRENCY_INSTANCE;
    private static Collator SENTENCE_COMPARATOR;

    private enum CATEGORY {
        SENTENCE {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getSentenceInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> true;
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) < 0;
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) > 0;
            }

            @Override
            int getValue(String value) {
                return value.length();
            }
        }, STRING {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getLineInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> true;
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) < 0;
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) > 0;
            }

            @Override
            int getValue(String value) {
                return value.length();
            }
        }, WORD {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getWordInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> true;
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) < 0;
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> SENTENCE_COMPARATOR.compare(relaxing, x) > 0;
            }

            @Override
            int getValue(String value) {
                return value.length();
            }
        }, NUMBER {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getWordInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> {
                    try {
                        NUMBER_INSTANCE.parse(element);
                        return true;
                    } catch (ParseException e) {
                        return false;
                    }
                };
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> {
                    try {
                        return NUMBER_INSTANCE.parse(relaxing).intValue() < NUMBER_INSTANCE.parse(x).intValue();
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> {
                    try {
                        return NUMBER_INSTANCE.parse(relaxing).intValue() > NUMBER_INSTANCE.parse(x).intValue();
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            int getValue(String value) {
                try {
                    return NUMBER_INSTANCE.parse(value).intValue();
                } catch (ParseException ignored) {
                }
                return 0;
            }
        }, MONEY {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getWordInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> {
                    try {
                        CURRENCY_INSTANCE.parse(element);
                        return true;
                    } catch (ParseException e) {
                        return false;
                    }
                };
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> {
                    try {
                        return CURRENCY_INSTANCE.parse(relaxing).intValue() < CURRENCY_INSTANCE.parse(x).intValue();
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> {
                    try {
                        return CURRENCY_INSTANCE.parse(relaxing).intValue() > CURRENCY_INSTANCE.parse(x).intValue();
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            int getValue(String value) {
                try {
                    return CURRENCY_INSTANCE.parse(value).intValue();
                } catch (ParseException ignored) {
                }
                return 0;
            }


        }, DATE {
            @Override
            BreakIterator getBreakIterator(Locale locale) {
                return BreakIterator.getWordInstance(locale);
            }

            @Override
            Predicate<String> filter(Locale locale) {
                return element -> {
                    try {
                        DATE_FORMAT.parse(element);
                        return true;
                    } catch (ParseException e) {
                        return false;
                    }
                };
            }

            @Override
            BiPredicate<String, String> relaxMax() {
                return (relaxing, x) -> {
                    try {
                        return DATE_FORMAT.parse(relaxing).before(DATE_FORMAT.parse(x));
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            BiPredicate<String, String> relaxMin() {
                return (relaxing, x) -> {
                    try {
                        return DATE_FORMAT.parse(relaxing).after(DATE_FORMAT.parse(x));
                    } catch (ParseException ignored) {
                    }
                    return false;
                };
            }

            @Override
            int getValue(String value) {
                return 0;
                // in chat say, what it can be ignored
            }
        };

        abstract BreakIterator getBreakIterator(Locale locale);

        abstract Predicate<String> filter(Locale locale);

        abstract BiPredicate<String, String> relaxMax();

        abstract BiPredicate<String, String> relaxMin();

        abstract int getValue(String value);
    }

    private static void writeStatWithBundle(BufferedWriter output, String s, ResourceBundle outputFileBundle, String add) throws IOException {
        output.write("\t" + outputFileBundle.getString(s) + add + "\n");
    }

    private static void textStatistic(Locale inputFileLocale, ResourceBundle outputFileBundle, String input, BufferedWriter output, String name_input) {
        try {
            output.write("<head>\n");
            output.write("\t<title>Statistics</title>\n");
            output.write("</head>\n");
            output.write("<body>\n");
            output.write("\t" + outputFileBundle.getString("Analyse_file") + ": " + name_input + "\n");
            for (CATEGORY category : CATEGORY.values()) {
                Set<String> uniq_words = new HashSet<>();
                int count = 0, min_length = input.length(), max_length = 0, sum = 0;
                String min = null, max = null;
                BreakIterator breakIterator = category.getBreakIterator(inputFileLocale);
                breakIterator.setText(input);
                BreakIterator statisticBreakIterator = (BreakIterator) breakIterator.clone();
                BiPredicate<String, String> comparatorMax = category.relaxMax();
                BiPredicate<String, String> comparatorMin = category.relaxMin();
                int start = statisticBreakIterator.first();
                for (int end = statisticBreakIterator.next(); end != BreakIterator.DONE; start = end, end = statisticBreakIterator.next()) {
                    String trimer = input.substring(start, end).trim();
                    if (category.filter(inputFileLocale).test(trimer)) {
                        count++;
                        uniq_words.add(trimer);
                        if (min == null) {
                            min = trimer;
                            max = trimer;
                        } else {
                            if (comparatorMax.test(max, trimer)) {
                                max = trimer;
                            }
                            if (comparatorMin.test(min, trimer)) {
                                min = trimer;
                            }
                        }
                        min_length = Math.min(min_length, trimer.length());
                        max_length = Math.max(max_length, trimer.length());
                        sum += category.getValue(trimer);

                    }
                }
                String lowerCategoryName = category.toString().toLowerCase();
                writeStatWithBundle(output, "Statistic_on_" + lowerCategoryName, outputFileBundle, "");
                writeStatWithBundle(output, "Count_" + lowerCategoryName, outputFileBundle, ": " + count);
                writeStatWithBundle(output, "Uniq_count_" + lowerCategoryName, outputFileBundle, ": " + uniq_words.size());
                writeStatWithBundle(output, "Min_" + lowerCategoryName, outputFileBundle, ": " + min);
                writeStatWithBundle(output, "Max_" + lowerCategoryName, outputFileBundle, ": " + max);
                writeStatWithBundle(output, "Min_Length_" + lowerCategoryName, outputFileBundle, ": " + min_length);
                writeStatWithBundle(output, "Max_Length_" + lowerCategoryName, outputFileBundle, ": " + max_length);
                writeStatWithBundle(output, "Average_" + lowerCategoryName, outputFileBundle, ": " + ((double) sum) / count);
                output.write("\n");
            }
            output.write("</body>\n");
        } catch (IOException exception) {
            System.out.println("Can't write!" + exception.getLocalizedMessage());
        } finally {
            try {
                output.close();
            } catch (IOException exception) {
                System.out.println("Can't close!" + exception.getLocalizedMessage());
            }
        }
    }

    private static void parsingError(String error) {
        System.out.println(error);
    }

    public static void main(String[] args) {
        ResourceBundle defaultBundle = null;
        try {
            defaultBundle = ResourceBundle.getBundle("ru.ifmo.rain.kuznetsov.i18n.StatisticsResourceBundle", Locale.getDefault());
        } catch (MissingResourceException ignored) {
        }
        if (defaultBundle == null) {
            defaultBundle = ResourceBundle.getBundle("ru.ifmo.rain.kuznetsov.i18n.StatisticsResourceBundle", Locale.US);
        }
        if (args == null) {
            parsingError(defaultBundle.getString("Not_null_args"));
            return;
        }
        if (args.length != 4) {
            parsingError(String.format(
                    "%s.\n\t %s: <%s> <%s> <%s> <%s>",
                    defaultBundle.getString("Incorrect_usage"),
                    defaultBundle.getString("Usage"),
                    defaultBundle.getString("text_locale"),
                    defaultBundle.getString("write_local"),
                    defaultBundle.getString("text_file"),
                    defaultBundle.getString("output_file")
            ));
            return;
        }
        Locale inputFileLocale = Locale.forLanguageTag(args[0]);
        ResourceBundle outputFileBundle;
        try {
            outputFileBundle = ResourceBundle.getBundle("ru.ifmo.rain.kuznetsov.i18n.StatisticsResourceBundle",
                    Locale.forLanguageTag(args[1]));
        } catch (MissingResourceException e) {
            parsingError(defaultBundle.getString("Missing_bundle_exception") + "\n" + e.getLocalizedMessage());
            return;
        }
        if (Files.notExists(Paths.get(args[2]))) {
            parsingError(defaultBundle.getString("Input_file_not_found"));
            return;
        }
        BufferedWriter output;
        try {
            output = Files.newBufferedWriter(Path.of(args[3]), StandardCharsets.UTF_8);
        } catch (IOException e) {
            parsingError(defaultBundle.getString("Input_file_not_found") + "\n" + e.getLocalizedMessage());
            return;
        }
        String input;
        try {
            input = Files.readString(Paths.get(args[2]), StandardCharsets.UTF_8);
        } catch (IOException e) {
            parsingError(e.getLocalizedMessage());
            return;
        }
        NUMBER_INSTANCE = NumberFormat.getNumberInstance(inputFileLocale);
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.DEFAULT, inputFileLocale);
        CURRENCY_INSTANCE = NumberFormat.getCurrencyInstance(inputFileLocale);
        SENTENCE_COMPARATOR = Collator.getInstance(inputFileLocale);
        textStatistic(inputFileLocale, outputFileBundle, input, output, args[3]);
    }
}
