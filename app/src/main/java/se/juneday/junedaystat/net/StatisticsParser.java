package se.juneday.junedaystat.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import se.juneday.junedaystat.domain.BooksSummary;
import se.juneday.junedaystat.domain.JunedayStat;

public class StatisticsParser {


    private static int getIntValue(JSONObject object, String key, int defaultValue) {
        int ret = defaultValue;
        try {
            ret = object.getInt(key);
        } catch (JSONException e) {
            ;
        }
        return ret;
    }

    public static JunedayStat jsonToJunedayStat(JSONObject json) {
        JSONObject summary ;
        try {
            summary = json.getJSONObject(JunedayStat.JDSTAT_BOOK_SUMMARY);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        int books = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_BOOKS, 0);
        int pages = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_PAGES, 0);
        int channels = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_CHANNELS, 0);
        int presentations = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_PRESENTATIONS, 0);
        int presPages = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_PRES_PAGES, 0);
        int videos = getIntValue(summary, JunedayStat.JDSTAT_BOOK_SUMMARY_VIDEOS, 0);

        BooksSummary bookSummary = new BooksSummary(books, pages, channels, presentations, presPages, videos);
        JunedayStat jds = new JunedayStat(bookSummary);

        return jds;
    }

}
