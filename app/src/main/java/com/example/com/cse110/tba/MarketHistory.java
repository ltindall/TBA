package com.example.com.cse110.tba;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 5/19/2015.
 */
public class MarketHistory extends Activity implements DBAsync
{
    private int listingType;
    private int ISBN;
    private DBManager manager;
    private GraphView gView;

    public static final int BUY_HISTORY = 0;
    public static final int SELL_HISTORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            listingType = extras.getInt("listingType");
            ISBN = extras.getInt("ISBN");
        }
        manager = new DBManager(this);
        gView = new GraphView(getApplicationContext());
        if(listingType == 0)
        {
            gView.setTitle("Buy Listings");
            gView.setTitleColor(Color.BLACK);
            gView.setTitleTextSize(50f);
        }
        else if(listingType == 1)
        {
            gView.setTitle("Sell Listings");
            gView.setTitleColor(Color.BLACK);
            gView.setTitleTextSize(50f);
        }
        /*
        LegendRenderer legend = new LegendRenderer(gView);
        gView.setLegendRenderer(legend);
        gView.getLegendRenderer().setVisible(true);
        gView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        //also need to add some text for legend!
        */

        /*GridLabelRenderer lables = new GridLabelRenderer(gView);
        lables.setHorizontalLabelsColor(Color.BLACK);
        lables.setVerticalLabelsColor(Color.BLACK);*/
        gView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        gView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        gView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));

        // display graph
        setContentView(R.layout.history_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
        layout.addView(gView);

        updateData();
    }

    public void updateData()
    {
        if(listingType == BUY_HISTORY)
        {
            manager.getBuyHistory(ISBN, -1);
        }
        else if(listingType == SELL_HISTORY)
        {
            manager.getSellHistory(ISBN, -1);
        }
    }

    private void setGraphData(Series s)
    {
        gView.removeAllSeries();
        gView.addSeries(s);
    }

    @Override
    public void onBuyListingsLoad(List<ParseObject> buyListings) {

    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {

    }

    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {
        if(buyHistory != null)
        {
            DataPoint[] data = new DataPoint[buyHistory.size()];
            for(int i = 0; i < buyHistory.size(); ++i)
            {
                data[i] = new DataPoint(buyHistory.get(i).getCreatedAt(),
                        buyHistory.get(i).getDouble("Price"));
            }
            LineGraphSeries<DataPoint> history = new LineGraphSeries<DataPoint>(data);

            setGraphData(history);
        }
    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {
        if(sellHistory != null)
        {
            DataPoint[] data = new DataPoint[sellHistory.size()];
            for(int i = 0; i < sellHistory.size(); ++i)
            {
                data[i] = new DataPoint(sellHistory.get(i).getCreatedAt(),
                        sellHistory.get(i).getDouble("Price"));
            }
            LineGraphSeries<DataPoint> history = new LineGraphSeries<DataPoint>(data);

            setGraphData(history);
        }
    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
