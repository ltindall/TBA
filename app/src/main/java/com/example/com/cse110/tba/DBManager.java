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

    public void addBook(String title, String author, int isbn, int price, int condition)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject book = new ParseObject("Book");
        book.add("Title", title);
        book.add("Author", author);
        book.add("ISBN", isbn);
        book.add("Price", price);
        book.add("Condition", condition);
        book.add("User", currentUser.getEmail());
        book.saveInBackground();
    }

    public void getBooks(String title, String author, int isbn)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
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

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> result, ParseException e) {
                if (e == null) {
                    caller.onGetBooksLoad(result);
                } else {
                    caller.onGetBooksLoad(null);
                }
            }
        });
    }
}
