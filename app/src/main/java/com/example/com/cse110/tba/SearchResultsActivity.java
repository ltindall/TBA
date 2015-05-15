package com.example.com.cse110.tba;

import android.app.Activity;
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
import java.util.Objects;

/**
 * Created by Alexander and Lucas on 5/4/2015.
 * Code for running the search function by taking a query
 */
public class SearchResultsActivity extends ListActivity implements DBAsync{
    private DBAsync dba;
    public DBManager dbm;
    private long currentSpinnerOption;

    public SearchResultsActivity() {
      dbm = new DBManager(dba);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent search = new Intent(Intent.ACTION_SEARCH);
        handleIntent(search);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent (intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            long searchBy = currentSpinnerOption;
            String s = String.valueOf(currentSpinnerOption);
            Log.d("SearchFunction", "currentSpinnerOption:" + s);
            switch ((int)searchBy) {
                case 0: dbm.getSellListings(query, null, -1, null);
                        break;
                case 1: dbm.getSellListings(null, query, -1, null);
                        break;
                case 2: dbm.getSellListings(null, null, Integer.parseInt(query), null);
                        break;
                case 3: dbm.getSellListings(null, null, -1, query);
                        break;
            }

        }

        else {
            Log.d("SearchFunction", "ACTION_SEARCH does not equal intent.getAction()");
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
            /*// Parse Query Adapter
            ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, "CustomBook");
            adapter.setTextKey("Title");
            //adapter.setImageKey("image");

            // Set the ListActivity's adapter to be the PQA

            ListView lv = (ListView)findViewById(android.R.id.list);

            if (lv == null) {
                Log.d("ListView", "BALLS");
            }

            else
                Log.d("ListView", "NO BALLS");

            lv.setAdapter(adapter);*/

            Log.d("SearchFunction", "FOUND");
        }

        else {
            Log.d("SearchFunction", "NOT FOUND");
        }
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
