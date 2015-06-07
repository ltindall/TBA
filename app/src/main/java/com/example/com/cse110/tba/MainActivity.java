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
import android.view.MotionEvent;
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
    private ParseSorter pSort;
    ListView lister;
    TabHost tabHost;
    SearchView searchView;
    public static ArrayAdapter<String> buyItemsAdapter;
    public static List<String> buyValues;
    public static ArrayAdapter<String> sellItemsAdapter;
    public static List<String> sellValues;

    private boolean creatingBuyListing = true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        dbm = new DBManager(this);
        pSort = new ParseSorter();
		setContentView(R.layout.activity_main);

        ParseUser current = ParseUser.getCurrentUser();
        if(current == null)
        {
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), LOGIN_PAGE);
        }

        /*ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), LOGIN_PAGE);*/

        // create the action bar and add items to it such as the spinner
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_spinner, R.layout.simple_spinner_dropdown_item_tba);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
        // actionBar end

        //get initial buy/sell listing. this is the first time user will see listing before doing anything
          //use DBManager to get any latest buy listing.
        //DBManager dbm = new DBManager(this);    // create DBManager object with MainActivity as its caller
          //call getBuyListing passing null arguments so query will not do any search
        dbm.getBuyListings(null,
                null,
                -1,
                "Title",  // sort the listing by title
                null,
                20);       // limit to 20 listings
        dbm.getSellListings(null, null, -1, "Title", null, 20);

        tabSetup();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if( ParseUser.getCurrentUser().get("email") != null ) {
            installation.put("user", ParseUser.getCurrentUser().get("email"));
        }
        installation.saveInBackground();
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
                Log.d("MainActivity", "Install ID: " + ParseInstallation.getCurrentInstallation().getInstallationId());
                Log.d("MainActivity", "Object ID: " + ParseInstallation.getCurrentInstallation().getInstallationId());
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

    /*public void launchPopup(View v)
    {
        /*
        ParseObject sampleListing = new ParseObject("BuyListing");
        ParseObject sampleBook = new ParseObject("CustomBook");
        sampleBook.put("Title", "Antigone");
        sampleBook.put("ISBN", 7616);
        sampleListing.put("Price", 9002);
        sampleListing.put("Book", sampleBook);
        ListingPopup popup = new ListingPopup(getApplicationContext(), sampleListing, v, false, values, position);
    }*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

        // Initialize: Search Stuff
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchView.requestFocusFromTouch();
                return true;
            }
        });
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(cn));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                //listAdapter.getFilter().filter(newText);
                Log.d("listening asdfawef", "listening.....");
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                Log.d("MainActivity", "onQueryTextSubmit");
                Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra("query", query);
                if (tabHost.getCurrentTab() == 0)
                    sellList = false;

                else
                    sellList = true;

                intent.putExtra("sellList", sellList);
                intent.putExtra("currentSpinnerItem", currentSpinnerItem);
                startActivity(intent);
                //listAdapter.getFilter().filter(query);
                return true;
            }
        }); // Search Stuff end

        ParseUser current = ParseUser.getCurrentUser();
        String email = current.getEmail();
        Log.d("MainActivity.java", "Current User: " + email);
        Log.d("MainActivity", "InstallID: " + ParseInstallation.getCurrentInstallation().get("user"));
        if (email == null){
            menu.add(Menu.NONE, 1, Menu.NONE, "Login");
        }
        else {
            // Inflate menu options
            menu.add(Menu.NONE, 0, Menu.NONE, "Account Settings");
            menu.add(Menu.NONE, 3, Menu.NONE, "Create Buy Listing");
            menu.add(Menu.NONE, 4, Menu.NONE, "Create Sell Listing");
            menu.add(Menu.NONE, 2, Menu.NONE, "My Listings");
            menu.add(Menu.NONE, 5, Menu.NONE, "Refresh List");
            menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        }
		return true;
	}

    /*
        Filters the Options Menu based on if the User is logged in
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.removeItem(0);
        menu.removeItem(1);
        menu.removeItem(2);
        menu.removeItem(3);
        menu.removeItem(4);
        menu.removeItem(5);

        ParseUser current = ParseUser.getCurrentUser();
        String email = current.getEmail();
        Log.d("MainActivity.java", "Current User: " + email);
        if (email == null) {
            menu.add(Menu.NONE, 1, Menu.NONE, "Login");
        } else {
            // Inflate menu options
            menu.add(Menu.NONE, 0, Menu.NONE, "User Settings");
            menu.add(Menu.NONE, 3, Menu.NONE, "Create Buy Listing");
            menu.add(Menu.NONE, 4, Menu.NONE, "Create Sell Listing");
            menu.add(Menu.NONE, 2, Menu.NONE, "My Listings");
            menu.add(Menu.NONE, 5, Menu.NONE, "Refresh List");
            menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent(MainActivity.this, UserSettings.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent2 = new Intent(MainActivity.this, MyListings.class);
                startActivity(intent2);
                break;
            case 1:
                ParseUser.logOut();
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), LOGIN_PAGE);
                break;
            case 3:  // Create buy listing
                creatingBuyListing = true;
                Intent intent3 = new Intent(MainActivity.this , CreateBuyingListing.class);
                startActivity(intent3);
                break;
            case 4:  // Create sell listing
                creatingBuyListing = false;
                Intent intent4 = new Intent(MainActivity.this , CreateSellingListing.class);
                startActivity(intent4);
                break;
            case 5:
                refresh();
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
    private ListView populateBuyListView(List<ParseObject> buyListings)
    {
        //setContentView(R.layout.activity_main);--> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMainBuy);
        final List<ParseObject> newListings;

        newListings = pSort.sortListings(buyListings, "Date", "BuyListing", 0);

        ArrayList<String> list = new ArrayList<String>();
        for(ParseObject listings: newListings) {
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

        //I see that this is pointless but I don't feel like fixing it. GG.
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
                ListingPopup popup = new ListingPopup(getApplicationContext(), newListings.get(position), view, false, buyValues, position);



            }

        });



        //buyItemsAdapter.notifyDataSetChanged();

        return lister;
        // make a list view and configure it with the created adapter
        // create a ListView data structure to contain the adapter
        /*ListView displayedListing = (ListView) findViewById(R.id.listViewMainPage);
        //set the adapter into the ListView

        displayedListing.setAdapter(itemsAdapter);*/
    }


    private void populateSellListView( final List<ParseObject> sellListings)
    {

        //setContentView(R.layout.activity_main);  --> do not set the content of activity so tabs won't be overwritten
        lister = (ListView)findViewById(R.id.listViewMainSell);
        final List<ParseObject> newListings;

        newListings = pSort.sortListings(sellListings, "Date", "SellListing", 0);

        ArrayList<String> list = new ArrayList<String>();
        if (sellListings == null)
            Log.d("MainActivity", "sellListings is null");
        for(ParseObject listings: newListings) {
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
                ListingPopup popup = new ListingPopup(getApplicationContext(), newListings.get(position), view, false, sellValues, position);

            }

        });

        //buyItemsAdapter.notifyDataSetChanged();

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

    /* this function will be called every time user is redirected to this page.*/
    @Override
    protected void onResume()
    {
        super.onResume();
        if(creatingBuyListing)
        {

            //get all buy listings made by this user
            dbm.getBuyListings(null,
                    null,
                    -1,
                    null,
                    "Title",
                    -1);
            if(buyItemsAdapter != null)
                buyItemsAdapter.notifyDataSetChanged();
            Log.d("BuyRefresh","Program Buy crash??");
        }
        else
        {
            //get all sell listings made by this user
            dbm.getSellListings(null,
                    null,
                    -1,
                    null,
                    "Title",
                    -1);
            if(sellItemsAdapter != null)
                sellItemsAdapter.notifyDataSetChanged();
            Log.d("SellRefresh","Program Sell crash????");
        }
    }

    protected void refresh()
    {
        dbm.getBuyListings(null,
                null,
                -1,
                null,
                "Title",
                -1);
        if(buyItemsAdapter != null)
            buyItemsAdapter.notifyDataSetChanged();

        dbm.getSellListings(null,
                null,
                -1,
                null,
                "Title",
                -1);
        if(sellItemsAdapter != null)
            sellItemsAdapter.notifyDataSetChanged();
    }
}
