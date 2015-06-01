package com.example.com.cse110.tba;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity implements  DBAsync, ActionBar.OnNavigationListener{

    public static final int LOGIN_PAGE = 0;
    private long currentSpinnerItem = 0;
    private boolean sellList = true;
    public DBManager dbm;
    ListView lister;
    TabHost tabHost;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        dbm = new DBManager(this);
		setContentView(R.layout.activity_main);

        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), LOGIN_PAGE);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_spinner, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);

        //get initial buy/sell listing. this is the first time user will see listing before doing anything
          //use DBManager to get any latest buy listing.
        DBManager dbm = new DBManager(this);    // create DBManager object with MainActivity as its caller
          //call getBuyListing passing null arguments so query will not do any search
        dbm.getBuyListings(null,
                null,
                -1,
                "Title",  // sort the listing by title
                null,
                10);       // limit to 10 listings
        dbm.getSellListings(null, null, -1, "Title", null, 10);

        tabSetup();
	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == LOGIN_PAGE && data != null)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // Associate the device with a user
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("user", ParseUser.getCurrentUser().get("email"));
                installation.saveInBackground();
                Log.d("MainActivity", "User: " + ParseUser.getCurrentUser().getUsername() + " successfully authenticated");
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                ParseAnonymousUtils.logInInBackground();
                // Associate the device with a user
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.remove("user");
                installation.saveInBackground();
                Log.d("MainActivity", "Authentication Failed, Creating Anon user");
            }
        }
        //DBManager manager = new DBManager(this);
        //manager.addBookListing(false,"Antigone","Sophocles",7616,9002,1,441,1,"Sample Text Please Ignore",true);
        //manager.setUserSettings(92092,null,null);
    }

    public void launchPopup(View v)
    {
        Log.d("MainActivity", "Button pressed");
        ParseObject sampleListing = new ParseObject("BuyListing");
        ParseObject sampleBook = new ParseObject("CustomBook");
        sampleBook.put("Title", "Antigone");
        sampleBook.put("ISBN", 7616);
        sampleListing.put("Price", 9002);
        sampleListing.put("Book", sampleBook);
        ListingPopup popup = new ListingPopup(getApplicationContext(), sampleListing, v);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

        // Initialize search stuff
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(cn));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                //listAdapter.getFilter().filter(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra("query", query);
                intent.putExtra("sellList", sellList);
                intent.putExtra("currentSpinnerItem", currentSpinnerItem);
                startActivity(intent);
                //listAdapter.getFilter().filter(query);
                return true;
            }
        });

        // Inflate menu options
        menu.add(Menu.NONE, 0, Menu.NONE, "Account Settings");
        menu.add(Menu.NONE, 2, Menu.NONE, "My Listings");
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent(MainActivity.this, UserSettings.class);
                startActivity(intent);
                return true;
            case 2:
                Intent intent2 = new Intent(MainActivity.this, MyListings.class);
                startActivity(intent2);
                return true;
            case 1:
                ParseUser.logOut();
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), LOGIN_PAGE);
                break;
        }
        return true;
    }

    @Override
    public void onBuyListingsLoad(List<ParseObject> buyListings) {
        //display the given List of Listings. remake the tabs
        populateBuyListView(buyListings);

    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {
        //display the given Listings. remake the tabs
        populateSellListView(sellListings);

    }

    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {
    }


    @Override
    public boolean onNavigationItemSelected(int i, long l)
    {
        currentSpinnerItem = l;
        Log.d("MainActivity", "currentSpinnerItem = " + Long.toString(currentSpinnerItem));
        return true;
    }


    //This functions fills the ListView with given Listing objects
    private ListView populateBuyListView( List<ParseObject> buyListings)
    {
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
            values[j] = list.get(j).toString();
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
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
        return lister;
    }


    private void populateSellListView( List<ParseObject> sellListings)
    {

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
            values[j] = list.get(j).toString();
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
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }


    /*
     * a method to set up the tabs and their contents. Links each tab with its corresponding listView
     */
    private void tabSetup()
    {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        //set up the tabs
        tabHost.setup();

        //tab for buy Listing
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("buylistingmain");
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

        // tab for sell Listing
        tabSpec = tabHost.newTabSpec("selllistingmain");
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

        tabHost.setCurrentTabByTag("selllistingmain");
    }




}
