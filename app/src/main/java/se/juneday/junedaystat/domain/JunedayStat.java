package se.juneday.junedaystat.domain;

/**
 * Created by hesa on 1/11/19.
 */

public class JunedayStat {

    public static final String JDSTAT_BOOK_SUMMARY = "book-summary";
    public static final String JDSTAT_BOOK_SUMMARY_BOOKS = "books";
    public static final String JDSTAT_BOOK_SUMMARY_PAGES = "pages";
    public static final String JDSTAT_BOOK_SUMMARY_CHANNELS = "uniq-channels";
    public static final String JDSTAT_BOOK_SUMMARY_PRESENTATIONS = "uniq-presentations";
    public static final String JDSTAT_BOOK_SUMMARY_PRES_PAGES = "uniq-presentations-pages";
    public static final String JDSTAT_BOOK_SUMMARY_VIDEOS = "uniq-videos";

    @Override
    public String toString() {
        return "JunedayStat{" +
                "booksSummary=" + booksSummary +
                '}';
    }

    public JunedayStat(BooksSummary booksSummary) {
        this.booksSummary = booksSummary;
    }

    public BooksSummary getBooksSummary() {
        return booksSummary;
    }

    private BooksSummary booksSummary;



}
