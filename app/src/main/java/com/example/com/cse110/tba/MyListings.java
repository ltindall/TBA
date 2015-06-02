package com.example.com.cse110.tba;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/* Created by Rachel Fisher on 5/26/15.
 */
public class MyListings extends Activity implements  DBAsync
{
    TabHost tabHost;
    public DBManager dbm;
    ListView lister;
    String email;

    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbm = new DBManager(this);

        ParseUser current = ParseUser.getCurrentUser();
        email = current.getEmail();
        Log.d("user", email);

        tabSetup();


    }

    private ListView populateBuyListView( List<ParseObject> buyListings){

        //setContentView(R.layout.activity_main);--> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMainBuy);

        ArrayList<String> list = new ArrayList<String>();
        for(ParseObject listings: buyListings) {
            ParseObject book = listings.getParseObject("Book");
            try {
                book.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //book.fetchIfNeeded();

            Log.d("book title string", book.getString("Title"));
            list.add(book.getString("Title")+" - "+book.getString("Author")+" - $"+listings.getNumber("Price"));

        }

        String[] values = new String[list.size()];
        for(int j =0; j<values.length; j++) {
            values[j] = list.get(j);
        }


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(itemsAdapter);

        // ListView Item Click Listener
        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                //int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        return lister;
    }

    private void populateSellListView( List<ParseObject> sellListings){


        //setContentView(R.layout.activity_main);  --> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMainSell);

        ArrayList<String> list = new ArrayList<String>();
        for(ParseObject listings: sellListings) {
            ParseObject book = listings.getParseObject("Book");
            try {
                book.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //book.fetchIfNeeded();

            Log.d("book title string", book.getString("Title"));
            list.add(book.getString("Title")+" - "+book.getString("Author")+" - $"+listings.getNumber("Price"));

        }

        String[] values = new String[list.size()];
        for(int j =0; j<values.length; j++) {
            values[j] = list.get(j);
        }


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(itemsAdapter);

        // ListView Item Click Listener
        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                //int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    private void tabSetup()
    {
        //get all buy listings made by this user
        dbm.getBuyListings(null,
                null,
                -1,
                null,
                email,
                -1);


        tabHost = (TabHost) findViewById(R.id.tabHost);
        //set up the tabs
        tabHost.setup();

        //tab for buy Listing
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("buylistingsmylistings");
        //creating the content of this tab
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                ListView buyListView = (ListView) findViewById(R.id.listViewMainBuy);
                return buyListView;
            }
        });
        tabSpec.setIndicator("Buy");  // name displayed on tab
        tabHost.addTab(tabSpec);  // add tab to tabHost

        final FrameLayout tabContent = tabHost.getTabContentView();

        for (int index = 0; index < tabContent.getChildCount(); index++){
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }

        //get all sell listings made by this user
        dbm.getSellListings(null,
                null,
                -1,
                null,
                email,
                -1);

        // tab for sell Listing
        tabSpec = tabHost.newTabSpec("selllistingmylistings");
        //set the tcontent of this tab
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                ListView sellListView = (ListView) findViewById(R.id.listViewMainSell);
                return sellListView;
            }
        });
        tabSpec.setIndicator("Sell");  // set the displayed name of the tab
        tabHost.addTab(tabSpec);  // add the tab to tabhost

        tabHost.setCurrentTabByTag("selllistingsmylistings");
    }

    @Override
    public void onBuyListingsLoad(List<ParseObject> buyListings) {
        //Log.d("MyListings", "buyListings" + buyListings.size());
        populateBuyListView(buyListings);
    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {
        populateSellListView(sellListings);
    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
