package com.example.com.cse110.tba;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class lisitingView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisiting_view);

        populateListView();
        registerOnClick();
    }

    /*
     * This method will populate a List data structure with Listing object and view it
     * on activity_listing_view.xml.
     * *****************************************************
     * reference video see:
     * https://www.youtube.com/watch?v=eAPFgC9URqc
     */

    private void populateListView()
    {
        //gather the Listing objects
          //get Listing objects from database

          //grab the first 10 Listing object from the returned objects above

          //store the 10 objects in an array of Listing objects

          //get a String from each Listing object
          String[] listingStrings = {"testListing1", "testListing2"};  // this is a stub

        //make an adapter containing a String from each Listing objects
          ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                                  this,         // a context for this activity
                                  R.layout.activity_lisiting_view, // layout to use
                                  listingStrings);  // objects to be dsplayed

        // Use ParseQuerAdapter<Listing>.
        // Call a function to get the query and display it
        // Automatically load from database in a certain timeframe(1 week) and reload if an older search is done...

        // make a list view and configure it with the created adapter
          // create a ListView dat astructure to contain the adapter
          ListView displayedListing = (ListView) findViewById(R.id.listViewListing);
          //set the adapter into the ListView
          displayedListing.setAdapter(listAdapter);
    }

    /*
     * This method will handle a Click event. When a ListView item is clicked, the click listener
     * will view a detailed information of the Lsitng object clicked
     */
    private void registerOnClick()
    {
        // grab the Listview from activity_listing_view.xml (id: listViewListing)

        // create a click listener and display the details of the Lsiting object when clicked


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lisiting_view, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
