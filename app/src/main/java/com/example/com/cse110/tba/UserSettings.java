package com.example.com.cse110.tba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 5/7/2015.
 */
public class UserSettings extends Activity implements  DBAsync
{
    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    private int currentZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load the xml layout, must be called before programatic use of buttons
        setContentView(R.layout.user_settings);

        //the zip code text field
        EditText zip = (EditText)findViewById(R.id.zip_code);

        //the 'call' number text field
        EditText call = (EditText)findViewById(R.id.call_number);

        //the 'text' number text field
        EditText text = (EditText)findViewById(R.id.text_number);

        //get current parse user and their current zip code
        ParseUser current = ParseUser.getCurrentUser();
        currentZip = current.getInt("Zipcode");

        //if they have a zip code, prepopulate the text box
        if(currentZip != 0)
        {
            zip.setText(Integer.toString(current.getInt("Zipcode")));
        }

        //if they have a call number, prepopulate the field
        if(current.getString("Call") != null)
        {
            call.setText(current.getString("Call"));
        }

        //if they have a text number, prepopulate the field
        if(current.getString("Text") != null)
        {
            text.setText(current.getString("Text"));
        }

        DBManager dbm = new DBManager(this);
        //dbm.addBookListing(false, "The Balls",  "", 8, 6, 1, 2015, 9000, "here you go Batman", true);
    }

    public void submit(View v)
    {
        DBManager manager = new DBManager(this);
        EditText zip = (EditText)findViewById(R.id.zip_code);
        EditText call = (EditText)findViewById(R.id.call_number);
        EditText text = (EditText)findViewById(R.id.text_number);
        int passZip = -1;
        if(zip.getText().length() != 0) {
            passZip = Integer.parseInt(zip.getText().toString().trim());
        }
        String passCall = call.getText().toString().trim();
        String passText = text.getText().toString().trim();
        if(currentZip == passZip)
        {
            passZip = -1;
        }

        if(passCall.length() == 0)
        {
            passCall = null;
        }
        if(passText.length() == 0)
        {
            passText = null;
        }

        manager.setUserSettings(passZip,passCall,passText);

        Toast.makeText(getApplicationContext(), "User Settings Updated",
                Toast.LENGTH_SHORT).show();

        ParseObject sampleListing = new ParseObject("SellListing");
        ParseObject sampleBook = new ParseObject("CustomBook");
        sampleBook.put("Title", "Antigone");
        sampleBook.put("ISBN", 7616);
        sampleListing.put("Price", 9002);
        sampleListing.put("Book", sampleBook);
        ListingPopup popup = new ListingPopup(getApplicationContext(), sampleListing, v);
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
