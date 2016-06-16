package com.linegraph.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.linegraph.R;
import com.linegraph.model.ValueList;
import com.linegraph.model.ViewGraphModel;
import com.linegraph.ui.AppDialog;
import com.linegraph.util.Config;
import com.linegraph.util.GsonRequest;
import com.linegraph.LineGraphApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =MainActivity.class.getSimpleName();
    private GraphView line_graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         line_graph = (GraphView) findViewById(R.id.graph);
        // API Call
           AppDialog.showProgress(this);
           graphdata();

    }



    private void graphdata() {
        GsonRequest<ViewGraphModel> modelGsonRequest = new GsonRequest<ViewGraphModel>
                (Request.Method.POST, Config.GRAPH_URL, ViewGraphModel.class, null, null,
                        new Response.Listener<ViewGraphModel>() {
                            @Override
                            public void onResponse(ViewGraphModel response) {
                                AppDialog.hideProgress();
                                List<ValueList> graphModel = response.getList();
                                DataPoint [] paramDataPoint1 = new DataPoint[10];
                                DataPoint [] paramDataPoint2 = new DataPoint[10];
                                //filling data points
                                    for (int i = 0; i < 10; i++) {
                                        paramDataPoint1[i]=new DataPoint(graphModel.get(i).getDatetime(),Double.valueOf(graphModel.get(i).getParam1()));
                                        paramDataPoint2[i]=new DataPoint(graphModel.get(i).getDatetime(),Double.valueOf(graphModel.get(i).getParam2()));
                                    }

                                // set date label formatter
                                DateFormat dateFormat = new SimpleDateFormat("\nyyyy-MM-dd\n\nhh:mm:ss", Locale.getDefault());
                                //plotting graph with 2 params
                                plotGraph(paramDataPoint1,paramDataPoint2);
                                //grid label customization
                                line_graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 3 because of the space
                                line_graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(MainActivity.this,dateFormat));
                                line_graph.getGridLabelRenderer().setTextSize(8f);
                                line_graph.getGridLabelRenderer().setLabelsSpace(20);
                                line_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
                                line_graph.getGridLabelRenderer().setNumVerticalLabels(6);
                                //setting viewport here
                                line_graph.getViewport().setMinY(0);
                                line_graph.getViewport().setMaxY(25);
                                line_graph.getViewport().setMinX(graphModel.get(0).getDatetime().getTime());
                                line_graph.getViewport().setMaxX(graphModel.get(9).getDatetime().getTime());
                                line_graph.getViewport().setXAxisBoundsManual(true);
                                line_graph.getViewport().setYAxisBoundsManual(true);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppDialog.hideProgress();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                        }
                        if (error instanceof TimeoutError) {
                            Log.e("Volley", "TimeoutError");
                        } else if (error instanceof NoConnectionError) {
                            Log.e("Volley", "NoConnectionError");
                        } else if (error instanceof AuthFailureError) {
                            Log.e("Volley", "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("Volley", "ServerError");
                        } else if (error instanceof NetworkError) {
                            Log.e("Volley", "NetworkError");
                        } else if (error instanceof ParseError) {
                            Log.e("Volley", "ParseError");
                        }
                    }
                }
                ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String mRequestBody = buildParams();
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
       LineGraphApp.getInstance().addToRequestQueue(modelGsonRequest, TAG);
    }

    private void plotGraph(DataPoint[] paramDataPoint1, DataPoint[] paramDataPoint2) {
        LineGraphSeries<DataPoint> line_series1 =
                new LineGraphSeries<DataPoint>(paramDataPoint1);
        LineGraphSeries<DataPoint> line_series2 =
                new LineGraphSeries<>(paramDataPoint2);
        line_series1.setColor(Color.RED);
        line_series2.setColor(Color.BLUE);
        //setting titles for legend
        line_series1.setTitle("param1");
        line_series2.setTitle("param2");
        line_graph.getLegendRenderer().setVisible(true);
        line_graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        //adding both series to graph
        line_graph.addSeries(line_series1);
        line_graph.addSeries(line_series2);
    }

    private String buildParams() {
        Map<String, String> params = new HashMap<String, String>();
        return buildRequestBody(params);
    }
    private String buildRequestBody(Object content) {
        String output = null;
        if ((content instanceof String) ||
                (content instanceof JSONObject) ||
                (content instanceof JSONArray)) {
            output = content.toString();
        } else if (content instanceof Map) {
            Uri.Builder builder = new Uri.Builder();
            HashMap hashMap = (HashMap) content;
            if (hashMap != null) {
                Iterator entries = hashMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    entries.remove();}
                output = builder.build().getEncodedQuery();
            }}
        return output;
    }
}
