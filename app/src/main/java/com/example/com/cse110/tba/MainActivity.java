package com.example.com.cse110.tba;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements  DBAsync, ActionBar.OnNavigationListener{

    public static final int LOGIN_PAGE = 0;
    //private long currentSpinnerItem = 0;
    SearchResultsActivity SRA = new SearchResultsActivity();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), LOGIN_PAGE);

        // Initialize Action Bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_spinner, android.R.layout.simple_spinner_dropdown_item);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == LOGIN_PAGE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // Associate the device with a user
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("user", ParseUser.getCurrentUser().get("email"));
                installation.saveInBackground();
                Log.d("MainActivity", "User: " + ParseUser.getCurrentUser().getUsername() + " successfully authenticated");
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                ParseAnonymousUtils.logInInBackground();
                // Associate the device with a user
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.remove("user");
                installation.saveInBackground();
                Log.d("MainActivity", "Authentication Failed, Creating Anon user");
            }
        }
        //DBManager manager = new DBManager(this);
        //manager.addBookListing(false,"Antigone","Sophocles",7616,9002,1,441,1,"Sample Text Please Ignore",true);
        //manager.setUserSettings(92092,null,null);
    }

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

        // Initialize Search Widget
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        final Intent theIntent = new Intent(MainActivity.this, SearchResultsActivity.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(theIntent);

                //menu.findItem(R.id.menu_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("setOnQueryTextListener", "SEARCHING");
                //listAdapter.getFilter().filter(query);
                return false;
            }

        });

        // Inflate menu options
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
            case 1:
                ParseUser.logOut();
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), LOGIN_PAGE);
                break;
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


    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        //currentSpinnerItem = l;
        SRA.setCurrentSpinnerOption(l);
        return true;
    }
}
