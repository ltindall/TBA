package com.example.com.cse110.tba;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Alexander and Lucas on 5/4/2015.
 * Code for running the search function by taking a query
 */
public class SearchResultsActivity extends ListActivity implements DBAsync{
    public DBManager dbm;
    private long currentSpinnerOption;

    public SearchResultsActivity() {
        Log.d("SearchResultsActivity", "An instance of SearchResultsActivity has been created.");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            long searchBy = currentSpinnerOption;
            switch ((int)searchBy) {
                case 0: dbm.getSellListings(query, null, -1, null,-1);
                        break;
                case 1: dbm.getSellListings(null, query, -1, null, -1);
                        break;
                case 2: dbm.getSellListings(null, null, Integer.parseInt(query), null, -1);
                        break;
                case 3: dbm.getSellListings(null, null, -1, query, -1);
                        break;
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
            if(sellListings.size() > 0)
                Log.d("SearchResultsActivity", "FOUND");

            else
                Log.d("SearchFunction", "NOT FOUND");
        }

        else
            Log.d("SearchFunction", "NULL OBJECT");
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

    public void setCurrentSpinnerOption(long l) {
        currentSpinnerOption = l;
    }
}
