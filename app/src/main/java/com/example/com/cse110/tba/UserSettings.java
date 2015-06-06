package com.example.com.cse110.tba;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

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
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Inflate menu options
        menu.add(Menu.NONE, 0, Menu.NONE, "Market Listings");
        menu.add(Menu.NONE, 3, Menu.NONE, "Create Buy Listing");
        menu.add(Menu.NONE, 4, Menu.NONE, "Create Sell Listing");
        menu.add(Menu.NONE, 2, Menu.NONE, "My Listings");
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case 1:
                ParseUser.logOut();
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), MainActivity.LOGIN_PAGE);
                break;
            case 2:
                Intent intent2 = new Intent(this, MyListings.class);
                startActivity(intent2);
                break;
            case 3:  // Create buy listing
                Intent intent3 = new Intent(this , CreateBuyingListing.class);
                startActivity(intent3);
                break;
            case 4:  // Create sell listing
                Intent intent4 = new Intent(this , CreateSellingListing.class);
                startActivity(intent4);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
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
