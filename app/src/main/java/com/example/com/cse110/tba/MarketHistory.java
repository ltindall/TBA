package com.example.com.cse110.tba;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.List;

/**
 * Created by Joshua Lynch on 5/19/2015.
 */
public class MarketHistory extends Activity implements DBAsync, ActionBar.OnNavigationListener {
    private int listingType;
    private int ISBN;
    private String Title;
    private DBManager manager;
    private GraphView gView;

    public static final int BUY_HISTORY = 0;
    public static final int SELL_HISTORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Title = "";
        if(extras != null)
        {
            listingType = extras.getInt("listingType");
            ISBN = extras.getInt("ISBN");
            Title = extras.getString("Title");
        }
        manager = new DBManager(this);
        gView = new GraphView(getApplicationContext());
        if(listingType == 0)
        {
            gView.setTitle("Buy Listings for " + Title);
            gView.setTitleColor(Color.BLACK);
            gView.setTitleTextSize(50f);
        }
        else if(listingType == 1)
        {
            gView.setTitle("Sell Listings for " + Title);
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

        gView.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        gView.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        gView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        gView.getGridLabelRenderer().setNumHorizontalLabels(3);

        // display graph
        setContentView(R.layout.history_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
        layout.addView(gView);

        updateData();

        // action bar to display the search bar and the spinner
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        return false;
    }
}
