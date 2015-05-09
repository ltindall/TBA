package com.example.com.cse110.tba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

    private int currentZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        EditText zip = (EditText)findViewById(R.id.zip_code);
        EditText call = (EditText)findViewById(R.id.call_number);
        EditText text = (EditText)findViewById(R.id.text_number);
        ParseUser current = ParseUser.getCurrentUser();
        currentZip = current.getInt("Zipcode");

        if(currentZip != 0)
        {
            zip.setText(Integer.toString(current.getInt("Zipcode")));
        }

        if(current.getString("Call") != null)
        {
            call.setText(current.getString("Call"));
        }

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
