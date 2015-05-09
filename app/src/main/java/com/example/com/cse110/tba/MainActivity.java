package com.example.com.cse110.tba;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity implements  DBAsync{

    public static final int LOGIN_PAGE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), LOGIN_PAGE);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == LOGIN_PAGE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Log.d("MainActivity", "User: " + ParseUser.getCurrentUser().getUsername() + " successfully authenticated");
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                ParseAnonymousUtils.logInInBackground();
                Log.d("MainActivity", "Authentication Failed, Creating Anon user");
            }
        }
        DBManager manager = new DBManager(this);
        //manager.addBookListing(true,"Antigone","Sophocles",7616,9001,1,441,1,"Sample Text Please Ignore",true);
        //manager.setUserSettings(92092,null,null);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Logout");

        // Initialize search stuff
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        // Inflate menu options
        //getMenuInflater().inflate(R.menu.options_menu, menu);
        menu.add(Menu.NONE, 0, Menu.NONE, "Account Settings");
        menu.add(Menu.NONE, 1, Menu.NONE, "Logout");
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 0:
                Intent intent = new Intent(MainActivity.this, UserSettings.class);
                startActivity(intent);
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

    // need to replace R.id.spinner with our own spinner id?  How do?
    Spinner spinner = (Spinner) findViewById(R.id.spinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.search_spinner, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spinner.setAdapter(adapter);
}
