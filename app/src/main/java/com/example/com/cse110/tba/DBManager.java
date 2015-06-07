package com.example.com.cse110.tba;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Joshua Lynch on 4/26/2015.
 * Manager Class for dealing with all database queries, has methods for downloading and uploading data,
 * paired with DBAsync to return data asynchronously
 */
public class DBManager
{
    //the calling class that instantiated this instance, should be 'this'
    private DBAsync caller;

    public DBManager(DBAsync c)
    {
        caller = c;
    }

    //add a book listing to the database
    public void addBookListing(boolean isBuyOrder, String title, String author, long isbn, float price,
                               int condition, int year, int edition, String comment, boolean isHardcover)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(isBuyOrder)
        {
            ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
            postACL.setPublicReadAccess(true);

            ParseObject book = new ParseObject("CustomBook");
            book.put("Title", title);
            book.put("Author", author);
            book.put("ISBN", isbn);
            book.put("Year", year);
            book.put("Edition", edition);
            book.setACL(postACL);

            ParseObject bookListing = new ParseObject("BuyListing");
            bookListing.put("Book", book);
            bookListing.put("Price", price);
            bookListing.put("Condition", condition);
            bookListing.put("Comment", comment);
            bookListing.put("HardCover", isHardcover);
            bookListing.put("isHistory", false);
            bookListing.put("User", currentUser.getEmail());
            bookListing.setACL(postACL);
            bookListing.saveInBackground();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("type", "BuyListing");
            params.put("isbn", isbn);
            params.put("price", price);

            //run cloud code to detect matching listing and contact its user
            ParseCloud.callFunctionInBackground("detectMatches", params);
        }
        else
        {
            ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
            postACL.setPublicReadAccess(true);

            ParseObject book = new ParseObject("CustomBook");
            book.put("Title", title);
            book.put("Author", author);
            book.put("ISBN", isbn);
            book.put("Year", year);
            book.put("Edition", edition);
            book.setACL(postACL);

            ParseObject bookListing = new ParseObject("SellListing");
            bookListing.put("Book", book);
            bookListing.put("Price", price);
            bookListing.put("Condition", condition);
            bookListing.put("Comment", comment);
            bookListing.put("HardCover", isHardcover);
            bookListing.put("isHistory", false);
            bookListing.put("User", currentUser.getEmail());
            bookListing.setACL(postACL);
            bookListing.saveInBackground();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("type", "SellListing");
            params.put("isbn", isbn);
            params.put("price", price);

            //run cloud code to detect matching listing and contact its user
            ParseCloud.callFunctionInBackground("detectMatches", params);
        }
    }

    public void getBuyListings(String title, String author, int isbn,
                                String order, String user, int limit)
    {
        ParseQuery<ParseObject> bookQuery = ParseQuery.getQuery("CustomBook");
        if(title != null)
        {
            bookQuery.whereEqualTo("Title", title);
        }
        if(author != null)
        {
            bookQuery.whereEqualTo("Author", author);
        }
        if(isbn != -1)
        {
            bookQuery.whereEqualTo("ISBN", isbn);
        }
        if(order != null)
        {
            if(order.equals("Title") || order.equals("Author") || order.equals("ISBN") ||
                    order.equals("Price") || order.equals("Condition"))
            {
                bookQuery.orderByAscending(order);
            }
        }

        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("BuyListing");

        if(user != null)
        {
            listQuery.whereEqualTo("User", user);
        }

        if(limit != -1)
        {
            listQuery.setLimit(limit);
        }

        listQuery.whereMatchesQuery("Book", bookQuery);
        listQuery.whereEqualTo("isHistory", false);
        listQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onBuyListingsLoad(result);
                } else {
                    caller.onBuyListingsLoad(null);
                }
            }
        });
    }

    public void getSellListings (String title, String author, int isbn,
                               String order, String user, int limit)
    {
        ParseQuery<ParseObject> bookQuery = ParseQuery.getQuery("CustomBook");
        if(title != null)
        {
            bookQuery.whereEqualTo("Title", title);
        }
        if(author != null)
        {
            bookQuery.whereEqualTo("Author", author);
        }
        if(isbn != -1)
        {
            bookQuery.whereEqualTo("ISBN", isbn);
        }
        if(order != null)
        {
            if(order.equals("Title") || order.equals("Author") || order.equals("ISBN") ||
                    order.equals("Price") || order.equals("Condition"))
            {
                bookQuery.orderByAscending(order);
            }
        }

        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("SellListing");


        if(user != null)
        {
            listQuery.whereEqualTo("User", user);
        }

        if(limit != -1)
        {
            listQuery.setLimit(limit);
        }

        listQuery.whereMatchesQuery("Book", bookQuery);
        listQuery.whereEqualTo("isHistory", false);
        listQuery.findInBackground(new FindCallback<ParseObject>() {
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

    public void getBuyHistory(int isbn, int limit)
    {
        ParseQuery<ParseObject> bookQuery = ParseQuery.getQuery("CustomBook");
        bookQuery.whereEqualTo("ISBN", isbn);
        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("BuyListing");
        listQuery.whereEqualTo("isHistory", true);
        listQuery.orderByAscending("createdAt");

        if(limit != -1)
        {
            listQuery.setLimit(limit);
        }

        listQuery.whereMatchesQuery("Book", bookQuery);
        listQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onBuyHistoryLoad(result);
                } else {
                    caller.onBuyHistoryLoad(null);
                }
            }
        });
    }

    public void getSellHistory(int isbn, int limit)
    {
        ParseQuery<ParseObject> bookQuery = ParseQuery.getQuery("CustomBook");
        bookQuery.whereEqualTo("ISBN", isbn);
        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("SellListing");
        listQuery.whereEqualTo("isHistory", true);
        listQuery.orderByAscending("createdAt");

        if(limit != -1)
        {
            listQuery.setLimit(limit);
        }

        //listQuery.whereMatchesQuery("Book", bookQuery);
        listQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onSellHistoryLoad(result);
                } else {
                    caller.onSellHistoryLoad(null);
                }
            }
        });
    }

    public static void notifyUser(ParseObject listing )
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        ParseObject book = listing.getParseObject("Book");
        try {
            book.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        params.put("book", book );
        params.put("listing", listing );
        params.put("email", ParseUser.getCurrentUser().getEmail());

        Log.d("hi josh", ParseUser.getCurrentUser().getEmail());

        //run cloud code to detect matching listing and contact its user
        ParseCloud.callFunctionInBackground("notifyUser", params);
        

    }

}
