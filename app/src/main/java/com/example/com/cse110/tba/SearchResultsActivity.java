package com.example.com.cse110.tba;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander and Lucas on 5/4/2015.
 * Code for running the search function by taking a query
 */
public class SearchResultsActivity extends Activity implements DBAsync{
    public DBManager dbm;
    ListView lister;

    public SearchResultsActivity() {
        Log.d("SearchResultsActivity", "An instance of SearchResultsActivity has been created.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.list_view);
        dbm = new DBManager(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent (intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            boolean sellOrBuy = intent.getBooleanExtra("sellList", true);
            long searchBy = intent.getLongExtra("currentSpinnerItem", 0);
            Log.d("SearchResultsActivity", "searchBy setting = " + Long.toString(searchBy));

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

    // how do we link the output of getSellListings to onSellListingsLoad

    // what is this going to do?
    public void onBuyListingsLoad(List<ParseObject> buyListings){
        if (buyListings != null) {
            // bring up the list of results of the buy listings that match the search.
        }

        else {
            // display search failure method
        }
    }
    // what is this going to do?
    public void onSellListingsLoad(List<ParseObject> sellListings){
        if (sellListings != null) {
            if(sellListings.size() > 0) {
                Log.d("SearchResultsActivity", "FOUND");
                populateListView(sellListings);

            }
            else {
                noResults();
                Log.d("SearchFunction", "NOT FOUND");
            }
        }

        else
            Log.d("SearchFunction", "NULL OBJECT");
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

        final List<String> values = new ArrayList<String>();
        for(int j =0; j<list.size(); j++) {
            values.add(list.get(j));
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

                ListingPopup popup = new ListingPopup(getApplicationContext(), sellListings.get(position), view, false, values, position);


                // ListView Clicked item index
                /*int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lister.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/

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

}
