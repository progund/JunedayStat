package se.juneday.junedaystat.activities;

// TODO: error feedback

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import se.juneday.junedaystat.R;
import se.juneday.junedaystat.domain.Book;
import se.juneday.junedaystat.domain.BooksSummary;
import se.juneday.junedaystat.domain.CodeSummary;
import se.juneday.junedaystat.domain.CodeSummary.ProgLang;
import se.juneday.junedaystat.domain.JunedayStat;
import se.juneday.junedaystat.domain.PodStat;
import se.juneday.junedaystat.domain.VideoStat;
import se.juneday.junedaystat.measurement.Measurement;
import se.juneday.junedaystat.net.StatisticsFetcher;
import se.juneday.junedaystat.utils.Utils;

public class OverviewActivity extends AppCompatActivity {

  private static final String LOG_TAG = OverviewActivity.class.getSimpleName();

  LocalDate startMeasurement;
  LocalDate stopMeasurement;
  LocalDate currentMeasurement;

  /*
  private static class Stat {

    JunedayStat stat;
    LocalDate date;
  }
*/
/*  private class Measurement {

    Stat start;
    Stat stop;

    private Measurement() {
      start = new Stat();
      stop = new Stat();
    }

    private Measurement(LocalDate start, LocalDate stop) {
      this();
      this.start.date = start;
      this.stopJunedayStat().date = stop;
    }
  }

  private Measurement createFromDate(LocalDate startDate, LocalDate stopDate) {
    Measurement meassurement = new Measurement();
    meassurement.start.date = startDate;
    meassurement.stopJunedayStat().date = stopDate;
    return meassurement;
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Measurement createFromDate(String startDate, String stopDate) {
    Measurement meassurement = new Measurement(Utils.stringToLocalDate(startDate),
        Utils.stringToLocalDate(stopDate));
    return meassurement;
  }

  private LocalDate now() {
    return Utils.stringToLocalDate("20190112");
//    return LocalDate.now();
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Measurement createFromDate(LocalDate startDate) {
    LocalDate now = now();
    Measurement meassurement = new Measurement(startDate, now);
    return meassurement;
  }


  @RequiresApi(api = VERSION_CODES.O)
  private Measurement createFromDate(int amount, ChronoUnit unit) {
    String startDate;
    String stopDate;
    LocalDate now = now();
    LocalDate then = now.minus(amount, unit);
    Measurement meassurement = new Measurement(then, now);

    return meassurement;
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Measurement extractMeasurement(Bundle bundle) {
    String startDate;
    String stopDate;

    LocalDate now = now();
    LocalDate then = now.minus(30, DAYS);

    Measurement meassurement = new Measurement();
    meassurement.start.date = then;
    meassurement.stopJunedayStat().date = now;

    return meassurement;
  }
*/

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.overview_menu, menu);
    return true;
  }


  @RequiresApi(api = VERSION_CODES.O)
  private void updateActivity(final LocalDate start, final LocalDate stop) {
    Log.d(LOG_TAG, "updateActivity()  " + start + " - " + stop);
    StatisticsFetcher fetcher = StatisticsFetcher.getInstance(this);
    fetcher.addMemberChangeListener(new StatisticsFetcher.StatisticsChangeListener() {
    Measurement meassurement = new Measurement();
      @Override
      public void onChange(LocalDate date, JunedayStat jds) {
        Log.d(LOG_TAG, "Got response: " + Utils.dateToString(date));
        Log.d(LOG_TAG, "Got from fetcher: " + date);
        Log.d(LOG_TAG, "Got from fetcher: " + Utils.dateToString(date) + " " + Utils.dateToString(start) + " " + Utils.dateToString(stop));
        if (start.equals(date)) {
          Log.d(LOG_TAG, "Got response: start:" + Utils.dateToString(date));
          meassurement.startJunedayStat(jds);
        } else {
          Log.d(LOG_TAG, "Got response: stop:" + Utils.dateToString(date));
          meassurement.stopJunedayStat(jds);
        }
        if (meassurement.startJunedayStat() != null && meassurement.stopJunedayStat() != null) {
          Log.d(LOG_TAG, "Got response: both: " + meassurement.stopJunedayStat().books().size() + " | " + meassurement.stopJunedayStat().books().size());
          updateViews(meassurement);
        }
      }
    });

    //
    fetcher.getStat(start);
    fetcher.getStat(stop);

  }

  @RequiresApi(api = VERSION_CODES.O)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Measurement m;
    LocalDate now = LocalDate.now();
    switch (item.getItemId()) {
      case R.id.dailly:
        updateActivity(now.minus(1, DAYS), now);
        return true;
      case R.id.weekly:
        updateActivity(now.minus(1, WEEKS), now);
        return true;
      case R.id.monthly:
        updateActivity(now.minus(1, MONTHS), now);
        return true;
      case R.id.yearly:
        updateActivity(now.minus(1, YEARS), now);
        return true;
      case R.id.year2017:
        updateActivity(Utils.stringToLocalDate("20170313"),Utils.stringToLocalDate( "20180101"));
        return true;
      case R.id.year2018:
        updateActivity(Utils.stringToLocalDate("20180101"),Utils.stringToLocalDate( "20190101"));
        return true;
      case R.id.alltime:
        updateActivity(Utils.stringToLocalDate("20170313"),now);
        return true;
      case R.id.user:
        getDatesFromUser();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  public void getStopDateFromUser() {
    DatePickerFragment dateStopFragment = new DatePickerFragment();
    dateStopFragment.setListener(new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        Log.d(LOG_TAG, "stop picker: " + y + " " + m + " " + d);
        Log.d(LOG_TAG,
            "stop picker: " + y + " " + Utils.formatMonth(m + 1) + " " + Utils
                .formatDay(d));
        stopMeasurement = Utils.stringToLocalDate(y +
            Utils.formatMonth(m + 1) +
            Utils.formatDay(d));
        updateActivity(startMeasurement, stopMeasurement);
      }
    });
    dateStopFragment.show(getSupportFragmentManager(), "Stop date");
  }

  public void getDatesFromUser() {
    Log.d(LOG_TAG, "start picker:");
    currentMeasurement = startMeasurement;
    DatePickerFragment dateStartFragment = new DatePickerFragment();
    dateStartFragment.setListener(new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        Log.d(LOG_TAG, "start picker: " + y + " " + m + " " + d);
        Log.d(LOG_TAG,
            "start picker: " + y + " " + Utils.formatMonth(m + 1) + " " + Utils
                .formatDay(d));
        startMeasurement = Utils.stringToLocalDate(y +
            Utils.formatMonth(m + 1) +
            Utils.formatDay(d));
        Log.d(LOG_TAG, "show stop picker");
        getStopDateFromUser();
      }
    });
    dateStartFragment.show(getSupportFragmentManager(), "Start date");
  }

  @RequiresApi(api = VERSION_CODES.O)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    LocalDate now = LocalDate.now();
    updateActivity(now.minus(1, WEEKS), now);

//    final Measurement meassurement = extractMeasurement(savedInstanceState);
  //  updateActivity(meassurement);
  }

  private void setViewTitle(View v, int outerId, String title) {
    TextView tv = v.findViewById(outerId);
    tv.setText(Html.fromHtml("<b>" + title + "</b>"));
  }

  private void setViewText(View v, int outerId, String title, int startVal, int stopVal, int days) {
    Log.d(LOG_TAG, "setViewText()   " + startVal + " " + stopVal + " days: " + days);
    View keyValueView = v.findViewById(outerId);
    TextView tv = keyValueView.findViewById(R.id.title);
    tv.setText(title);
    tv = keyValueView.findViewById(R.id.value);
    int diff = stopVal - startVal;
    double ratio = ((double) diff) / days;
    Log.d(LOG_TAG, "setViewText()   " + stopVal + " - " + startVal + " = " + diff + " => " + ratio);
    //    tv.setText(String.valueOf(stopVal - startVal));
    tv.setText("" + diff + "  (" + Utils.doubleToString(ratio) + ")");
  }

  private void setViewText(View v, int outerId, String title, int diff, int days) {
    Log.d(LOG_TAG, "setViewText()   " + diff);
    View keyValueView = v.findViewById(outerId);
    TextView tv = keyValueView.findViewById(R.id.title);
    tv.setText(title);
    tv = keyValueView.findViewById(R.id.value);
    double ratio = ((double) diff) / days;
    tv.setText("" + diff + "  (" + Utils.doubleToString(ratio) + ")");
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateViews(Measurement meassurement) {
    View scrollLayout = findViewById(R.id.scroll_layout);
    View outerLayout = scrollLayout.findViewById(R.id.summary_layout);

    View dateLayout = scrollLayout.findViewById(R.id.date_layout);
    TextView date = dateLayout.findViewById(R.id.date_title);
    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());

    date.setText(Html.fromHtml(
        "<h1>" + meassurement.startJunedayStat().date() + " - " + meassurement.stopJunedayStat().date() + "<br>" + days
            + " days </h1>"));

    updateBookSummaryView(meassurement, outerLayout);
    updateCodeSummaryView(meassurement, outerLayout);
    updateVideoSummaryView(meassurement, outerLayout);
    updatePodSummary(meassurement, outerLayout);
    updateBooksList(meassurement, outerLayout);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateVideoSummaryView(Measurement meassurement, View lookupView) {
    VideoStat start = meassurement.startJunedayStat().videoSummary();
    VideoStat stop = meassurement.stopJunedayStat().videoSummary();

    setViewTitle(lookupView, R.id.video_title, "Video");

    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());
    Log.d(LOG_TAG, " videos: "
        + meassurement.startJunedayStat().videoSummary().videos()
        + " " + meassurement.stopJunedayStat().videoSummary().videos() + " days:" + days + "  "
        + meassurement.startJunedayStat().date() + " " + meassurement.stopJunedayStat().date());
    View videoLayout = lookupView.findViewById(R.id.video_count);

    setViewText(videoLayout, R.id.video_count, "Videos: ",
        meassurement.startJunedayStat().videoSummary().videos(),
        meassurement.stopJunedayStat().videoSummary().videos(), days);
  }

  private List<String> bookInfo(Measurement meassurement) {
    List<String> books = new ArrayList<>();
    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());

    Set<String> titles = Measurement.bookTitlesUnion(meassurement.startJunedayStat().books(), meassurement.stopJunedayStat().books());
    for (String title : titles) {
      Book startBook = meassurement.findBook(meassurement.startJunedayStat().books(), title);
      Book stopBook = meassurement.findBook(meassurement.stopJunedayStat().books(), title);

      int stopPages = 0;
      int startPages = 0;
      try {
        startPages = startBook.pages();
      } catch (Exception e) {
        ; // already has 0 as default
      }
      try {
        stopPages = stopBook.pages();
      } catch (Exception e) {
        ; // already has 0 as default
      }

      Log.d(LOG_TAG, "book: " + title + " " + startPages + " - " + stopPages);
      int diff = stopPages - startPages;
      double ratio = ((double) diff) / days;

      books.add(title + ": " + diff + "  ( " + Utils.doubleToString(ratio ) + ")");
    }

    return books;
  }

  private void updateBooksList(Measurement meassurement, View lookupView) {
    View bookLayout = lookupView.findViewById(R.id.books_layout);
    setViewTitle(lookupView, R.id.books_title, "Books");

    // Lookup ListView
    ListView listView = (ListView) lookupView.findViewById(R.id.book_list);

    // Create Adapter
    ArrayAdapter adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, bookInfo(meassurement));

    // Set listView's adapter to the new adapter
    listView.setAdapter(adapter);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updatePodSummary(Measurement meassurement, View lookupView) {
    View bookLayout = lookupView.findViewById(R.id.book_layout);
    PodStat start = meassurement.startJunedayStat().podStat();
    PodStat stop = meassurement.stopJunedayStat().podStat();

    setViewTitle(lookupView, R.id.pod_title, "Pod");

    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());
    View videoLayout = lookupView.findViewById(R.id.pod_count);

    Log.d(LOG_TAG, "pod: " + meassurement.startJunedayStat().podStat().podCasts());
    Log.d(LOG_TAG, "pod: " + meassurement.stopJunedayStat().podStat().podCasts());

    setViewText(videoLayout,
        R.id.pod_count, "Videos: ",
        meassurement.startJunedayStat().podStat().podCasts(),
        meassurement.stopJunedayStat().podStat().podCasts(),
        days);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateCodeSummaryView(Measurement meassurement, View lookupView) {
    CodeSummary start = meassurement.startJunedayStat().codeSummary();
    CodeSummary stop = meassurement.stopJunedayStat().codeSummary();

    setViewTitle(lookupView, R.id.code_title, "Source code");

    View codeLayout = lookupView.findViewById(R.id.code_layout);

        /*
        Set<String> langs = new HashSet<>();
        Iterator<CodeSummary.Stat> iter = startJunedayStat().iterator();
        while (iter.hasNext()) {new
            langs.add(iter.next().getLang());
        }
        iter = stopJunedayStat().iterator();
        while (iter.hasNext()) {
            langs.add(iter.next().getLang());
        }
*/
    int startJava = meassurement.startJunedayStat().codeSummary().loc(CodeSummary.ProgLang.Java);
    int stopJava = meassurement.stopJunedayStat().codeSummary().loc(CodeSummary.ProgLang.Java);

    int startC = meassurement.startJunedayStat().codeSummary().loc(CodeSummary.ProgLang.C);
    int stopC = meassurement.stopJunedayStat().codeSummary().loc(CodeSummary.ProgLang.C);

    int startBash = meassurement.startJunedayStat().codeSummary().loc(ProgLang.Bash);
    int stopBash = meassurement.stopJunedayStat().codeSummary().loc(ProgLang.Bash);

    int startBuild = meassurement.startJunedayStat().codeSummary().loc(ProgLang.Bash);
    int stopBuild = meassurement.stopJunedayStat().codeSummary().loc(ProgLang.Build);


    int startSum = startJava + startC + startBash;
    int stopSum = stopJava + stopC + stopBash;

    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());

    setViewText(codeLayout, R.id.loc_java, "Java: ", startJava, stopJava, days);
    setViewText(codeLayout, R.id.loc_c, "C: ", startC, stopC, days);
    setViewText(codeLayout, R.id.loc_bash, "Bash: ", startBash, stopBash, days);
    setViewText(codeLayout, R.id.loc_total, "Total: ", startSum, stopSum, days);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateBookSummaryView(Measurement meassurement, View lookupView) {
    BooksSummary start = meassurement.startJunedayStat().booksSummary();
    BooksSummary stop = meassurement.stopJunedayStat().booksSummary();

    View bookLayout = lookupView.findViewById(R.id.book_layout);

    setViewTitle(lookupView, R.id.book_title, "Books");

    int days = (int) DAYS.between(meassurement.startJunedayStat().date(), meassurement.stopJunedayStat().date());

    setViewText(bookLayout, R.id.books_summary, "Books: ",
        meassurement.startJunedayStat().books().size(),
        meassurement.stopJunedayStat().books().size(),
        days);
    setViewText(bookLayout, R.id.pages_summary, "Pages: ",
        meassurement.startJunedayStat().pages(),
        meassurement.stopJunedayStat().pages(),
        days);

    Log.d(LOG_TAG, "Channels: " + meassurement.startJunedayStat().channels() + " " + meassurement.stopJunedayStat().channels());
    setViewText(bookLayout, R.id.channels_summary, "Channels: ",
        meassurement.startJunedayStat().channels(),
        meassurement.stopJunedayStat().channels(),
        days);
    setViewText(bookLayout, R.id.presentation_summary, "Presentations: ",
        meassurement.startJunedayStat().presentations(),
        meassurement.stopJunedayStat().presentations(),
        days);
    setViewText(bookLayout, R.id.presentation_pages_summary, "Presentation pages: ",
        meassurement.startJunedayStat().presentationsPages(),
        meassurement.stopJunedayStat().presentationsPages(),
        days);
  }

  public static class DatePickerFragment extends DialogFragment
      /*      implements DatePickerDialog.OnDateSetListener */ {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
      this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      Calendar cal = Calendar.getInstance();
      datePickerDialog = new DatePickerDialog(getActivity(),
          android.R.style.Theme_Holo_Dialog,
          listener,
          cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH),
          cal.get(Calendar.DAY_OF_MONTH));

      return datePickerDialog;
    }

    /*
    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
      OverviewActivity.this.currentMeasurement =
          Utils.stringToLocalDate(String.valueOf(m) + String.valueOf(y) + String.valueOf(d));
    }
    */
  }

}
