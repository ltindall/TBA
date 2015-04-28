package com.example.com.cse110.tba;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 4/26/2015.
 */
public class DBManager
{
    private DBAsync caller;
    public DBManager(DBAsync c)
    {
        caller = c;
    }

    public void addBookListing(boolean isBuyOrder, String title, String author, int isbn, float price,
                               int condition)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(isBuyOrder)
        {
            ParseObject book = new ParseObject("BuyListing");
            book.add("Title", title);
            book.add("Author", author);
            book.add("ISBN", isbn);
            book.add("Price", price);
            book.add("Condition", condition);
            book.add("User", currentUser.getEmail());
            book.saveInBackground();
        }
        else
        {
            ParseObject book = new ParseObject("SellListing");
            book.add("Title", title);
            book.add("Author", author);
            book.add("ISBN", isbn);
            book.add("Price", price);
            book.add("Condition", condition);
            book.add("User", currentUser.getEmail());
            book.saveInBackground();
        }
    }

    public void getBuyListings(String title, String author, int isbn,
                                String order)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BuyListing");
        if(title != null)
        {
            query.whereEqualTo("Title", title);
        }
        if(author != null)
        {
            query.whereEqualTo("Author", author);
        }
        if(isbn != -1)
        {
            query.whereEqualTo("ISBN", isbn);
        }
        if(order != null)
        {
            if(order.equals("Title") || order.equals("Author") || order.equals("ISBN") ||
                    order.equals("Price") || order.equals("Condition"))
            {
                query.orderByAscending(order);
            }
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onBuyListingsLoad(result);
                } else {
                    caller.onBuyListingsLoad(null);
                }
            }
        });
    }

    public void getSellListings(String title, String author, int isbn,
                               String order)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SellListing");
        if(title != null)
        {
            query.whereEqualTo("Title", title);
        }
        if(author != null)
        {
            query.whereEqualTo("Author", author);
        }
        if(isbn != -1)
        {
            query.whereEqualTo("ISBN", isbn);
        }
        if(order != null)
        {
            if(order.equals("Title") || order.equals("Author") || order.equals("ISBN") ||
                    order.equals("Price") || order.equals("Condition"))
            {
                query.orderByAscending(order);
            }
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onSellListingsLoad(result);
                } else {
                    caller.onSellListingsLoad(null);
                }
            }
        });
    }

    public void getUser(String email)
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    caller.onUserLoad(users);
                } else {
                    caller.onUserLoad(null);
                }
            }
        });
    }
}
