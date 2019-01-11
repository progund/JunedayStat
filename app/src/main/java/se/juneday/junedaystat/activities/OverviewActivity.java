package se.juneday.junedaystat.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.juneday.junedaystat.R;
import se.juneday.junedaystat.domain.BooksSummary;
import se.juneday.junedaystat.domain.CodeSummary;
import se.juneday.junedaystat.domain.JunedayStat;
import se.juneday.junedaystat.net.StatisticsFetcher;

public class OverviewActivity extends AppCompatActivity {

    private static final String LOG_TAG = OverviewActivity.class.getSimpleName();


    private static class Stat {
        JunedayStat stat;
        String date;
    }

    private class Meassurement {
        Stat start;
        Stat stop;

        private Meassurement() {
            start = new Stat();
            stop = new Stat();
        }
    }

    private Meassurement getMeasurement(Bundle bundle) {
        String startDate = "20190101";
        String stopDate = "20190111";

        Meassurement meassurement = new Meassurement();
        meassurement.start.date = startDate;
        meassurement.stop.date = stopDate;

        return meassurement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Meassurement meassurement = getMeasurement(savedInstanceState);

        StatisticsFetcher fetcher = StatisticsFetcher.getInstance(this);
        fetcher.addMemberChangeListener(new StatisticsFetcher.StatisticsChangeListener() {
            @Override
            public void onChange(String date, JunedayStat jds) {
                if (meassurement.start.date.equals(date)) {
                    meassurement.start.stat = jds;
                } else {
                    meassurement.stop.stat = jds;
                }
                if ( meassurement.start.stat!=null && meassurement.stop.stat!=null ) {
                    updateViews(meassurement);
                }
            }
        });

        //
        fetcher.getStat(meassurement.start.date);
        fetcher.getStat(meassurement.stop.date);
    }


    private void setViewText(View v, int outerId, String title, int startVal, int stopVal) {
        Log.d(LOG_TAG, "setViewText()   " + startVal + " " + stopVal);
        View keyValueView = v.findViewById(outerId);
        TextView tv = keyValueView.findViewById(R.id.title);
        tv.setText(title);
        tv = keyValueView.findViewById(R.id.value);
        tv.setText(String.valueOf(stopVal-startVal));
    }

    private void updateViews(Meassurement meassurement) {
        View scrollLayout = findViewById(R.id.scroll_layout);
        View outerLayout = scrollLayout.findViewById(R.id.summary_layout);

        View dateLayout = scrollLayout.findViewById(R.id.date_layout);
        TextView date = dateLayout.findViewById(R.id.date_title);
        date.setText(meassurement.start.date + " - " + meassurement.stop.date);

        updateBookSummaryView(meassurement, outerLayout);
        updateCodeSummaryView(meassurement, outerLayout);
    }

    private void updateCodeSummaryView(Meassurement meassurement, View lookupView) {
        CodeSummary start = meassurement.start.stat.getCodeSummary();
        CodeSummary stop = meassurement.stop.stat.getCodeSummary();

        View codeLayout = lookupView.findViewById(R.id.code_layout);

        /*
        Set<String> langs = new HashSet<>();
        Iterator<CodeSummary.Stat> iter = start.iterator();
        while (iter.hasNext()) {
            langs.add(iter.next().getLang());
        }
        iter = stop.iterator();
        while (iter.hasNext()) {
            langs.add(iter.next().getLang());
        }
*/

        setViewText(codeLayout, R.id.loc_java, "Java: ", start.stat(CodeSummary.LANG_JAVA).getLoc(), stop.stat(CodeSummary.LANG_JAVA).getLoc());
        setViewText(codeLayout, R.id.loc_c, "C: ", start.stat(CodeSummary.LANG_C).getLoc(), stop.stat(CodeSummary.LANG_C).getLoc());
        setViewText(codeLayout, R.id.loc_bash, "Bash: ",start.stat(CodeSummary.LANG_BASH).getLoc(), stop.stat(CodeSummary.LANG_BASH).getLoc());
    }
    private void updateBookSummaryView(Meassurement meassurement, View lookupView) {
        BooksSummary start = meassurement.start.stat.getBooksSummary();
        BooksSummary stop = meassurement.stop.stat.getBooksSummary();

        View bookLayout = lookupView.findViewById(R.id.book_layout);

        setViewText(bookLayout, R.id.books_summary, "Books: ", start.getBooks(), stop.getBooks());
        setViewText(bookLayout, R.id.pages_summary, "Pages: ", start.getPages(), stop.getPages());
        setViewText(bookLayout, R.id.channels_summary, "Channels: ", start.getChannels(), stop.getChannels());
        setViewText(bookLayout, R.id.presentation_summary, "Presentations: ", start.getPages(), stop.getPages());
        setViewText(bookLayout, R.id.presentation_pages_summary, "Presentation pages: ", start.getPresentationsPages(), stop.getPresentationsPages());
    }

}
