package com.example.com.cse110.tba;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

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
    List<String> buyValues;
    List<String> sellValues;
    ArrayAdapter<String> buyItemsAdapter;
    ArrayAdapter<String> sellItemsAdapter;


    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_listings);

        dbm = new DBManager(this);

        ParseUser current = ParseUser.getCurrentUser();
        email = current.getEmail();
        Log.d("user", email);

        //get all buy listings made by this user
        dbm.getBuyListings(null,
                null,
                -1,
                null,
                email,
                -1);

        //get all sell listings made by this user
        dbm.getSellListings(null,
                null,
                -1,
                null,
                email,
                -1);

        tabSetup();
    }

    private ListView populateBuyListView( final List<ParseObject> buyListings){

        //setContentView(R.layout.activity_main);--> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMyListBuy);

        final ArrayList<String> list = new ArrayList<String>();
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

        buyValues = new ArrayList<String>();
        for(int j =0; j<list.size(); j++) {
            buyValues.add(list.get(j));
        }


        buyItemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, buyValues);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(buyItemsAdapter);

        // ListView Item Click Listener
        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListingPopup popup = new ListingPopup(getApplicationContext(), buyListings.get(position), view, true, buyValues, position);

                buyItemsAdapter.notifyDataSetChanged();
            }

        });


        return lister;
    }

    private void populateSellListView( final List<ParseObject> sellListings){


        //setContentView(R.layout.activity_main);  --> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMyListSell);

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

        sellValues = new ArrayList<String>();
        for(int j =0; j<list.size(); j++) {
            sellValues.add(list.get(j));
        }


        sellItemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sellValues);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(sellItemsAdapter);

        // ListView Item Click Listener
        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListingPopup popup = new ListingPopup(getApplicationContext(), sellListings.get(position), view, true, sellValues, position);

                sellItemsAdapter.notifyDataSetChanged();
            }

        });
    }

    private void tabSetup()
    {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        //set up the tabs
        //tabHost.clearAllTabs();
        tabHost.setup();

        //tab for buy Listing
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("buylistingsmylistings");
        //creating the content of this tab
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                ListView buyListView = (ListView) findViewById(R.id.listViewMyListBuy);
                return buyListView;
            }
        });
        tabSpec.setIndicator("Buy");  // name displayed on tab
        tabHost.addTab(tabSpec);  // add tab to tabHost

        // tab for sell Listing
        tabSpec = tabHost.newTabSpec("selllistingsmylistings");
        //set the tcontent of this tab
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                ListView sellListView = (ListView) findViewById(R.id.listViewMyListSell);
                return sellListView;
            }
        });
        tabSpec.setIndicator("Sell");  // set the displayed name of the tab
        tabHost.addTab(tabSpec);  // add the tab to tabhost

        tabHost.setCurrentTabByTag("selllistingsmylistings");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Inflate menu options
        menu.add(Menu.NONE, 0, Menu.NONE, "Market Listings");
        menu.add(Menu.NONE, 3, Menu.NONE, "Create Buy Listing");
        menu.add(Menu.NONE, 4, Menu.NONE, "Create Sell Listing");
        menu.add(Menu.NONE, 2, Menu.NONE, "User Settings");
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case 1:
                ParseUser.logOut();
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), MainActivity.LOGIN_PAGE);
                break;
            case 2:
                Intent intent2 = new Intent(this, UserSettings.class);
                startActivity(intent2);
                break;
            case 3:  // Create buy listing
                Intent intent3 = new Intent(this , CreateBuyingListing.class);
                startActivity(intent3);
                break;
            case 4:  // Create sell listing
                Intent intent4 = new Intent(this , CreateSellingListing.class);
                startActivity(intent4);
                break;
        }
        return true;
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
