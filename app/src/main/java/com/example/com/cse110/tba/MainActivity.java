package com.example.com.cse110.tba;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.app.ActionBar;
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

import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity implements  DBAsync, ActionBar.OnNavigationListener{

    public static final int LOGIN_PAGE = 0;
    private long currentSpinnerItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
                             -1 ,
                            "Title",  // sort the listing by title
                            10);       // limit to 10 listings


        // set the onClickListener for each item of ListView
        registerOnClick();

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
                startActivity(intent);
                //listAdapter.getFilter().filter(query);
                return true;
            }
        });

        // Inflate menu options
        menu.add(Menu.NONE, 0, Menu.NONE, "Account Settings");
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
        //display the given List of Listings
        populateListView(buyListings);

    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {
        //display the given Listings
        populateListView(sellListings);

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
        return true;
    }


    //This functions fills the ListView with given Listing objects
    private void populateListView( List<ParseObject> listofListing)
    {
        // listofListing is a list of Listing object that wants to be displayed.
        //make an adapter containing a String from each Listing objects

        ListingAdapter listAdapter = new ListingAdapter(this, listofListing);

        // make a list view and configure it with the created adapter
        // create a ListView data structure to contain the adapter
        ListView displayedListing = (ListView) findViewById(R.id.listViewMainPage);
        //set the adapter into the ListView
        displayedListing.setAdapter(listAdapter);
    }

    /*
     * This method will handle a Click event. When a ListView item is clicked, the click listener
     * will view a detailed information of the Lsitng object clicked
     */
    private void registerOnClick()
    {
        // grab the ListView
        ListView displayedListing = (ListView) findViewById(R.id.listViewMainPage);

        // create a click listener and display the details of the Lsiting object when clicked
        displayedListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //make a ListingPopup object to show the clicked Listing
                ListingPopup popup = new ListingPopup(MainActivity.this,   // Context
                        (ParseObject) parent.getAdapter().getItem(position), //ParseObject to be displayed. How to get the ParseObject???????
                        viewClicked);   // View where popup will be shown
            }
        });


    }


}
