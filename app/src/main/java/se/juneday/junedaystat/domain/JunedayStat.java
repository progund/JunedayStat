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


    public static final String JDSTAT_SOURCE_CODE = "source-code";
    public static final String JDSTAT_SOURCE_CODE_LOC = "lines-of-code";
    public static final String JDSTAT_SOURCE_CODE_FILES = "number-of-files";
    public static final String JDSTAT_SOURCE_CODE_TYPE = "type";


    @Override
    public String toString() {
        return "JunedayStat{" +
                "booksSummary=" + booksSummary +
                "codeSummary=" + codeSummary +
                '}';
    }

    public JunedayStat(BooksSummary booksSummary, CodeSummary codeSummary) {
        this.booksSummary = booksSummary;
        this.codeSummary = codeSummary;
    }

    public BooksSummary getBooksSummary() {
        return booksSummary;
    }

    public CodeSummary getCodeSummary() {
        return codeSummary;
    }

    private BooksSummary booksSummary;
    private CodeSummary codeSummary;


}
