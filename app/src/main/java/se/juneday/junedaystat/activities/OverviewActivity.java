package se.juneday.junedaystat.activities;

// TODO: error feedback

import static java.time.temporal.ChronoUnit.DAYS;

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
import java.util.List;
import java.util.Map;
import se.juneday.junedaystat.R;
import se.juneday.junedaystat.domain.Book;
import se.juneday.junedaystat.domain.BooksSummary;
import se.juneday.junedaystat.domain.CodeSummary;
import se.juneday.junedaystat.domain.JunedayStat;
import se.juneday.junedaystat.domain.PodStat;
import se.juneday.junedaystat.domain.VideoStat;
import se.juneday.junedaystat.net.StatisticsFetcher;
import se.juneday.junedaystat.utils.Utils;

public class OverviewActivity extends AppCompatActivity {

  private static final String LOG_TAG = OverviewActivity.class.getSimpleName();

  LocalDate startMeasurement;
  LocalDate stopMeasurement;
  LocalDate currentMeasurement;

  private static class Stat {

    JunedayStat stat;
    LocalDate date;
  }

  private class Meassurement {

    Stat start;
    Stat stop;

    private Meassurement() {
      start = new Stat();
      stop = new Stat();
    }

    private Meassurement(LocalDate start, LocalDate stop) {
      this();
      this.start.date = start;
      this.stop.date = stop;
    }
  }

  private Meassurement createFromDate(LocalDate startDate, LocalDate stopDate) {
    Meassurement meassurement = new Meassurement();
    meassurement.start.date = startDate;
    meassurement.stop.date = stopDate;
    return meassurement;
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Meassurement createFromDate(String startDate, String stopDate) {
    Meassurement meassurement = new Meassurement(Utils.stringToLocalDate(startDate),
        Utils.stringToLocalDate(stopDate));
    return meassurement;
  }

  private LocalDate now() {
    return Utils.stringToLocalDate("20190112");
//    return LocalDate.now();
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Meassurement createFromDate(LocalDate startDate) {
    LocalDate now = now();
    Meassurement meassurement = new Meassurement(startDate, now);
    return meassurement;
  }


  @RequiresApi(api = VERSION_CODES.O)
  private Meassurement createFromDate(int amount, ChronoUnit unit) {
    String startDate;
    String stopDate;
    LocalDate now = now();
    LocalDate then = now.minus(amount, unit);
    Meassurement meassurement = new Meassurement(then, now);

    return meassurement;
  }

  @RequiresApi(api = VERSION_CODES.O)
  private Meassurement extractMeasurement(Bundle bundle) {
    String startDate;
    String stopDate;

    LocalDate now = now();
    LocalDate then = now.minus(30, DAYS);

    Meassurement meassurement = new Meassurement();
    meassurement.start.date = then;
    meassurement.stop.date = now;

    return meassurement;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.overview_menu, menu);
    return true;
  }


  @RequiresApi(api = VERSION_CODES.O)
  private void updateActivity(final Meassurement meassurement) {
    Log.d(LOG_TAG, "updateActivity()  " + meassurement.start.date + " - " + meassurement.stop.date);
    StatisticsFetcher fetcher = StatisticsFetcher.getInstance(this);
    fetcher.addMemberChangeListener(new StatisticsFetcher.StatisticsChangeListener() {
      @Override
      public void onChange(LocalDate date, JunedayStat jds) {
        Log.d(LOG_TAG, "Got from fetcher: " + date);
        Log.d(LOG_TAG,
            "Got from fetcher, start: " + meassurement.start.date + " " + meassurement.start.stat);
        Log.d(LOG_TAG,
            "Got from fetcher, stop: " + meassurement.stop.date + " " + meassurement.stop.stat);
        if (meassurement.start.date.equals(date)) {
          meassurement.start.stat = jds;
        } else {
          meassurement.stop.stat = jds;
        }
        if (meassurement.start.stat != null && meassurement.stop.stat != null) {
          updateViews(meassurement);
        }
      }
    });

    //
    fetcher.getStat(meassurement.start.date);
    fetcher.getStat(meassurement.stop.date);

  }

  @RequiresApi(api = VERSION_CODES.O)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Meassurement m;
    switch (item.getItemId()) {
      case R.id.dailly:
        m = createFromDate(1, DAYS);
        updateActivity(m);
        return true;
      case R.id.weekly:
        m = createFromDate(7, DAYS);
        updateActivity(m);
        return true;
      case R.id.monthly:
        m = createFromDate(1, ChronoUnit.MONTHS);
        updateActivity(m);
        return true;
      case R.id.yearly:
        m = createFromDate(1, ChronoUnit.YEARS);
        updateActivity(m);
        return true;
      case R.id.year2017:
        m = createFromDate("20170313", "20180101");
        updateActivity(m);
        return true;
      case R.id.year2018:
        m = createFromDate("20180101", "20190101");
        updateActivity(m);
        return true;
      case R.id.alltime:
        m = createFromDate(Utils.stringToLocalDate("20170313"));
        updateActivity(m);
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
        updateActivity(createFromDate(startMeasurement, stopMeasurement));
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

    final Meassurement meassurement = extractMeasurement(savedInstanceState);
    updateActivity(meassurement);
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
  private void updateViews(Meassurement meassurement) {
    View scrollLayout = findViewById(R.id.scroll_layout);
    View outerLayout = scrollLayout.findViewById(R.id.summary_layout);

    View dateLayout = scrollLayout.findViewById(R.id.date_layout);
    TextView date = dateLayout.findViewById(R.id.date_title);
    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);

    date.setText(Html.fromHtml(
        "<h1>" + meassurement.start.date + " - " + meassurement.stop.date + "<br>" + days
            + " days </h1>"));

    updateBookSummaryView(meassurement, outerLayout);
    updateCodeSummaryView(meassurement, outerLayout);
    updateVideoSummaryView(meassurement, outerLayout);
    updatePodSummary(meassurement, outerLayout);
    updateBooksList(meassurement, outerLayout);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateVideoSummaryView(Meassurement meassurement, View lookupView) {
    VideoStat start = meassurement.start.stat.getVideoSummary();
    VideoStat stop = meassurement.stop.stat.getVideoSummary();

    setViewTitle(lookupView, R.id.video_title, "Video");

    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);
    Log.d(LOG_TAG, " videos: " + start.videos() + " " + stop.videos() + " days:" + days + "  "
        + meassurement.start.date + " " + meassurement.stop.date);
    View videoLayout = lookupView.findViewById(R.id.video_count);

    setViewText(videoLayout, R.id.video_count, "Videos: ", start.videos(), stop.videos(), days);
  }

  private List<String> bookInfo(Meassurement meassurement) {
    List<String> books = new ArrayList<>();
    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);

    Map<String, Integer> bookPages = new HashMap<>();
    for (Book b : meassurement.start.stat.books()) {
      Log.d(LOG_TAG, "Adding to map: " + b.name() + " " + b.pages());
      bookPages.put(b.name(), b.pages());
    }

    for (Book b : meassurement.stop.stat.books()) {
      Log.d(LOG_TAG, "Adding to list: " + b.name() + " " + b.pages());
      String name = b.name();
      int stopPages = b.pages();

      int startPages = 0;
      try {
        startPages = bookPages.get(name);
      } catch (Exception e) {
        ; // already has 0 as default
      }

      Log.d(LOG_TAG, "book: " + b.name() + " " + startPages + " - " + stopPages);
      int diff = stopPages - startPages;
      double ratio = ((double) diff) / days;

      books.add(name + ": " + diff + "(" + Utils.doubleToString(ratio ) + ")");
    }

    return books;
  }

  private void updateBooksList(Meassurement meassurement, View lookupView) {
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
  private void updatePodSummary(Meassurement meassurement, View lookupView) {
    View bookLayout = lookupView.findViewById(R.id.book_layout);
    PodStat start = meassurement.start.stat.podStat();
    PodStat stop = meassurement.stop.stat.podStat();

    setViewTitle(lookupView, R.id.pod_title, "Pod");

    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);
    View videoLayout = lookupView.findViewById(R.id.pod_count);

    Log.d(LOG_TAG, "pod: " + start.podCasts());
    Log.d(LOG_TAG, "pod: " + stop.podCasts());

    setViewText(videoLayout, R.id.pod_count, "Videos: ", start.podCasts(), stop.podCasts(), days);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateCodeSummaryView(Meassurement meassurement, View lookupView) {
    CodeSummary start = meassurement.start.stat.getCodeSummary();
    CodeSummary stop = meassurement.stop.stat.getCodeSummary();

    setViewTitle(lookupView, R.id.code_title, "Source code");

    View codeLayout = lookupView.findViewById(R.id.code_layout);

        /*
        Set<String> langs = new HashSet<>();
        Iterator<CodeSummary.Stat> iter = start.iterator();
        while (iter.hasNext()) {new
            langs.add(iter.next().getLang());
        }
        iter = stop.iterator();
        while (iter.hasNext()) {
            langs.add(iter.next().getLang());
        }
*/
    int startJava = getLoc(start.stat(CodeSummary.LANG_JAVA));
    int stopJava = getLoc(stop.stat(CodeSummary.LANG_JAVA));
    int startC = getLoc(start.stat(CodeSummary.LANG_C));
    int stopC = getLoc(stop.stat(CodeSummary.LANG_C));
    int startBash = getLoc(start.stat(CodeSummary.LANG_BASH));
    int stopBuild = getLoc(stop.stat(CodeSummary.LANG_BUILD));
    int startBuild = getLoc(start.stat(CodeSummary.LANG_BUILD));
    int stopBash = getLoc(stop.stat(CodeSummary.LANG_BASH));

    int startSum = startJava + startC + startBash;
    int stopSum = stopJava + stopC + stopBash;

    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);

    setViewText(codeLayout, R.id.loc_java, "Java: ", startJava, stopJava, days);
    setViewText(codeLayout, R.id.loc_c, "C: ", startC, stopC, days);
    setViewText(codeLayout, R.id.loc_bash, "Bash: ", startBash, stopBash, days);
    setViewText(codeLayout, R.id.loc_total, "Total: ", startSum, stopSum, days);
  }

  private int getLoc(CodeSummary.Stat stat) {
    if (stat == null) {
      return 0;
    }
    return stat.getLoc();
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void updateBookSummaryView(Meassurement meassurement, View lookupView) {
    BooksSummary start = meassurement.start.stat.getBooksSummary();
    BooksSummary stop = meassurement.stop.stat.getBooksSummary();

    View bookLayout = lookupView.findViewById(R.id.book_layout);

    setViewTitle(lookupView, R.id.book_title, "Books");

    int days = (int) DAYS.between(meassurement.start.date, meassurement.stop.date);

    setViewText(bookLayout, R.id.books_summary, "Books: ", start.getBooks(), stop.getBooks(), days);
    setViewText(bookLayout, R.id.pages_summary, "Pages: ", start.getPages(), stop.getPages(), days);
    setViewText(bookLayout, R.id.channels_summary, "Channels: ", start.getChannels(),
        stop.getChannels(), days);
    setViewText(bookLayout, R.id.presentation_summary, "Presentations: ", start.getPresentations(),
        stop.getPresentations(), days);
    setViewText(bookLayout, R.id.presentation_pages_summary, "Presentation pages: ",
        start.getPresentationsPages(), stop.getPresentationsPages(), days);
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
