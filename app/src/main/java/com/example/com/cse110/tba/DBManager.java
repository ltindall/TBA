package com.example.com.cse110.tba;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
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
                               int condition, int year, int edition, String comment, boolean isHardcover)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(isBuyOrder)
        {
            ParseObject book = new ParseObject("CustomBook");
            book.put("Title", title);
            book.put("Author", author);
            book.put("ISBN", isbn);
            book.put("Year", year);
            book.put("Edition", edition);

            ParseObject bookListing = new ParseObject("BuyListing");
            bookListing.put("Book", book);
            bookListing.put("Price", price);
            bookListing.put("Condition", condition);
            bookListing.put("Comment", comment);
            bookListing.put("HardCover", isHardcover);
            bookListing.put("User", currentUser.getEmail());
            bookListing.saveInBackground();
        }
        else
        {
            ParseObject book = new ParseObject("CustomBook");
            book.put("Title", title);
            book.put("Author", author);
            book.put("ISBN", isbn);
            book.put("Year", year);
            book.put("Edition", edition);

            ParseObject bookListing = new ParseObject("SellListing");
            bookListing.put("Book", book);
            bookListing.put("Price", price);
            bookListing.put("Condition", condition);
            bookListing.put("Comment", comment);
            bookListing.put("HardCover", isHardcover);
            bookListing.put("User", currentUser.getEmail());
            bookListing.saveInBackground();
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

    public void setUserSettings(int zipCode, String call, String text)
    {
        int validParamCount = 0;
        ParseUser current = ParseUser.getCurrentUser();
        if(zipCode != -1)
        {
            ++validParamCount;
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ZipcodeDB");
            query.whereEqualTo("Zip", zipCode);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> zip, ParseException e) {
                    if (e == null) {
                        ParseGeoPoint location = new ParseGeoPoint(zip.get(0).getDouble("Latitude"),
                                zip.get(0).getDouble("Longitude"));
                        ParseUser current = ParseUser.getCurrentUser();
                        current.put("Location", location);
                        current.saveInBackground();
                    } else {
                    }
                }
            });
            current.put("Zipcode", zipCode);
        }
        if(call != null)
        {
            ++validParamCount;
            current.put("Call", call);
        }
        if(text != null)
        {
            ++validParamCount;
            current.put("Text", text);
        }
        if(validParamCount > 0)
        {
            current.saveInBackground();
        }
    }
}
