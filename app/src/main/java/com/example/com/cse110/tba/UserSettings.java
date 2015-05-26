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


        Intent intent = new Intent(this, MarketHistory.class);
        intent.putExtra("listingType", MarketHistory.SELL_HISTORY);
        intent.putExtra("ISBN", 7616);
        startActivity(intent);
    }

    public void submit(View v)
    {
        DBManager manager = new DBManager(this);
        EditText zip = (EditText)findViewById(R.id.zip_code);
        EditText call = (EditText)findViewById(R.id.call_number);
        EditText text = (EditText)findViewById(R.id.text_number);
        int passZip = Integer.parseInt(zip.getText().toString().trim());
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
    public void onBuyListingsLoad(List<ParseObject> buyListings) {


    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {

    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
