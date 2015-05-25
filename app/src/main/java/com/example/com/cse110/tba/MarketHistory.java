package com.example.com.cse110.tba;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
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
        }
        else if(listingType == 1)
        {
            gView.setTitle("Sell Listings");
        }
        // display graph
        setContentView(R.layout.history_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
        layout.addView(gView);
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
        LineGraphSeries history = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < buyHistory.size(); ++i)
        {
            history.appendData(new DataPoint(buyHistory.get(i).getDate("createdAt"),
                                            buyHistory.get(i).getDouble("Price")), true, 50);
            setGraphData(history);
        }
    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {
        LineGraphSeries history = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < sellHistory.size(); ++i)
        {
            history.appendData(new DataPoint(sellHistory.get(i).getDate("createdAt"),
                    sellHistory.get(i).getDouble("Price")), true, 50);
            setGraphData(history);
        }
    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
