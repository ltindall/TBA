package com.example.com.cse110.tba;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Alexander and Lucas on 5/4/2015.
 * Code for running the search function by taking a query
 */
public class SearchResultsActivity extends Activity implements DBAsync{
    private DBAsync dba;
    public DBManager dbm = new DBManager(dba);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            dbm.getSellListings(query, null, -1, null);
            dbm.getSellListings(null, query, -1, null);
            dbm.getSellListings(null, null, Integer.parseInt(query), null);
            dbm.getSellListings(null, null, -1, query);

        }
    }

    // what is this going to do?
    public void onBuyListingsLoad(List<ParseObject> buyListings){

    }
    // what is this going to do?
    public void onSellListingsLoad(List<ParseObject> sellListings){

    }
    // what is this going to do?
    public void onUserLoad(List<ParseUser> userList){

    }
}
