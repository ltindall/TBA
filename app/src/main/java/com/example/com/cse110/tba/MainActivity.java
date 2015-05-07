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
import android.widget.SearchView;

import java.util.List;

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
