package com.example.com.cse110.tba;


import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/* Created by Rachel Fisher on 5/26/15.
 */
public class MyListings extends Activity implements  DBAsync
{
    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        DBManager dbm = new DBManager(this);

        ParseUser current = ParseUser.getCurrentUser();
        String email = current.getEmail();

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


    }

    @Override
    public void onBuyListingsLoad(List<ParseObject> buyListings) {

    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {

    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
