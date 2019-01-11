package se.juneday.junedaystat.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import se.juneday.junedaystat.domain.JunedayStat;

public class StatisticsFetcher {

    private static final String LOG_TAG = StatisticsFetcher.class.getSimpleName();

    private RequestQueue queue;
    private static StatisticsFetcher instance;
    private Context context;

    private String dateToUrl(String date) {
        return "http://rameau.sandklef.com/junedaywiki-stats/" + date + "/jd-stats.json";
    }


    private StatisticsFetcher(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        listeners = new ArrayList<>();
    }

    public static StatisticsFetcher getInstance(Context context) {
        if (instance==null) {
            instance = new StatisticsFetcher(context);
        }
        return instance;
    }

    public void getStat(final String date) {
        final String url = dateToUrl(date);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "Got response: " + response.toString().length());
                        JunedayStat jds = StatisticsParser.jsonToJunedayStat(response);
                        Log.d(LOG_TAG, "Got JunedayStat: " + jds);
                        for (StatisticsChangeListener s : listeners) {
                            s.onChange(date, jds);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }


    private List<StatisticsChangeListener> listeners;

    public interface StatisticsChangeListener {
        void onChange(String date, JunedayStat jds);
    }

    public void addMemberChangeListener(StatisticsChangeListener l) {
        listeners.add(l);
    }

}
