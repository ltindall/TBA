package com.example.com.cse110.tba;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 4/26/2015.
 */
public interface DBAsync
{
    abstract void onBuyListingsLoad(List<ParseObject> buyListings);

    abstract void onSellListingsLoad(List<ParseObject> sellListings);

    abstract void onUserLoad(List<ParseUser> userList);
}
