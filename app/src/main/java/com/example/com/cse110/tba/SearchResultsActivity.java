package com.example.com.cse110.tba;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Orr and Lucas Tindall on 5/4/2015.
 * Code for running the search function by taking a query
 */
public class SearchResultsActivity extends Activity implements DBAsync, ActionBar.OnNavigationListener{
    public DBManager dbm;
    private long currentSpinnerItem; // global variable that determines which filter to search by:
                                     //     book, author, isbn, order
    private boolean sellOrBuy; // global bool to determine whether the search query is originating
                               // from the 'sell' tab or the 'buy' tab
    ListView lister;

    /*public SearchResultsActivity() {
        Log.d("SearchResultsActivity", "An instance of SearchResultsActivity has been created.");
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.list_view);
        dbm = new DBManager(this);

        // action bar to display the search bar and the spinner
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_spinner, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);

        Log.d("SearchResultsActivity", "onCreate entered");
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Initialize Search Bar
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
                intent.putExtra("currentSpinnerItem", currentSpinnerItem);
                handleIntent(intent);

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
    protected void onNewIntent(Intent intent) {
        setIntent (intent);
        handleIntent(intent);
    }

    /*
     * method: handleIntent
     * description: handles the search intent by passing the query, sellOrBuy boolean, and searchBy
     *              long to query either the sellListing or buyListing database to see if the query
     *              has a match.  searchBy determines whether the query is a book, author, isbn or
     *              order.
     */
    private void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            sellOrBuy = intent.getBooleanExtra("sellList", true);
            long searchBy = intent.getLongExtra("currentSpinnerItem", 0);
            Log.d("SearchResultsActivity", "searchBy setting = " + Long.toString(searchBy));

            // branch for searching sell listings
            if (sellOrBuy) {
                switch ((int) searchBy) {
                    case 0:
                        dbm.getSellListings(query, null, -1, null, null, -1);
                        break;
                    case 1:
                        dbm.getSellListings(null, query, -1, null, null, -1);
                        break;
                    case 2:
                        dbm.getSellListings(null, null, Integer.parseInt(query), null, null, -1);
                        break;
                    case 3:
                        dbm.getSellListings(null, null, -1, query, null, -1);
                        break;
                }
            }

            // branch for searching buy listings
            else {
                switch ((int) searchBy) {
                    case 0:
                        dbm.getBuyListings(query, null, -1, null, null, -1);
                        break;
                    case 1:
                        dbm.getBuyListings(null, query, -1, null, null, -1);
                        break;
                    case 2:
                        dbm.getBuyListings(null, null, Integer.parseInt(query), null, null, -1);
                        break;
                    case 3:
                        dbm.getBuyListings(null, null, -1, query, null, -1);
                        break;
                }
            }
        }

        else {
            Log.d("SearchFunction","Current Intent = " + intent.getAction());
        }
    }

    /*
     * method: onBuyListingsLoad
     * description: once the getBuyListings method returns a list of parse objects
     *              onBuyListingsLoad will check the contents of the list and pass
     *              it on to the display in order to display the listings that match
     *              the query
     */
    public void onBuyListingsLoad(List<ParseObject> buyListings){
        if (buyListings != null) {
             if(buyListings.size() > 0) {
                Log.d("SearchResultsActivity", "FOUND");
                populateListView(buyListings);
            }
            else
                Log.d("SearchResultsActivity", "NOT FOUND");
        }

        else {
            Log.d("SearchResultsActivity", "NULL OBJECT");
        }
    }

    /*
     * method: onSellListingsLoad
     * description: once the getSellListings method returns a list of parse objects
     *              onSellListingsLoad will check the contents of the list and pass
     *              it on to the display in order to display the listings that match
     *              the query
     */
    public void onSellListingsLoad(List<ParseObject> sellListings){

        Log.d("SearchResultsActivity", "This function is being visited");
        if (sellListings != null) {
            if(sellListings.size() > 0) {
                Log.d("SearchResultsActivity", "FOUND");
                populateListView(sellListings);
            }
            else
                Log.d("SearchResultsActivity", "NOT FOUND");
            else {
                noResults();
                Log.d("SearchFunction", "NOT FOUND");
            }
        }

        else
            Log.d("SearchResultsActivity", "NULL OBJECT");
    }

    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    private void noResults() {
        setContentView(R.layout.list_view);
        lister = (ListView)findViewById(R.id.list);

        String[] values = new String[1];
        values[0] = "No results found.";

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(itemsAdapter);

        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                /*
                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Nothing was found, stop clicking", Toast.LENGTH_LONG)
                        .show();
                */

            }

        });

    }

    private void populateListView(final List<ParseObject> sellListings){
        setContentView(R.layout.list_view);
        lister = (ListView)findViewById(R.id.list);

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

//        String[] values = new String[] { "josdjfoasjdfjasdfopjasfd",
//                "alexxxxxx",
//                "Lucass"
//        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data


         ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);


        //Log.d("SearchResultsActivity", "ENTERED LIST VIEW");
        lister.setAdapter(itemsAdapter);

        // ListView Item Click Listener
        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ListingPopup popup = new ListingPopup(getApplicationContext(), sellListings.get(position), view);



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

    // what is this going to do?
    public void onUserLoad(List<ParseUser> userList){
        if (userList != null) {
            // show list of users
        }

        else {
            // display search failure method
        }
    }

    //Used to check if query statement is an int or not an int
    //WILL ONLY BE USED IF THE SPINNER CANNOT BE IMPLEMENTED PROPERLY
    /*public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }*/

    /*
     * method: onNavigationItemSelected
     * description: determines which spinner item has been selected
     */
    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        currentSpinnerItem = l;
        Log.d("MainActivity", "currentSpinnerItem = " + Long.toString(currentSpinnerItem));
        return true;
    }
}
