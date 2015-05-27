package com.example.com.cse110.tba;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 4/26/2015.
 */
public interface DBAsync
{
    void onBuyListingsLoad(List<ParseObject> buyListings);

    void onSellListingsLoad(List<ParseObject> sellListings);

    void onBuyHistoryLoad(List<ParseObject> buyHistory);

    void onSellHistoryLoad(List<ParseObject> sellHistory);

    void onUserLoad(List<ParseUser> userList);
}
