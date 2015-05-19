/*
    This class extends Application so that it is run once as a global Singleton Object.
    The Parse initialization his handled here, no other Activity needs to initialize it.
 */
package com.example.com.cse110.tba;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    private static String PARSE_APPLICATION_ID = "9Sejg1tgXT9qRkeS3uqoykI9E84kVb8DAlCPzsNi";
    private static String PARSE_CLIENT_KEY = "MtMjrzfToRzCGf7mzFtnPXlApnUFBAqEGPmty8bm";
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, false);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}