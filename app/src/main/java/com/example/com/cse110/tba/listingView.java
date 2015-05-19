package com.example.com.cse110.tba;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class listingView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

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

        //make an adapter containing a String from each Listing objects
          /*ParseQueryAdapter<ParseObject> listAdapter = new ParseQueryAdapter<ParseObject>(
                  this,         // a context for this activity
                  new ParseQueryAdapter.QueryFactory<ParseObject>() {   // a Query generator that will call search function
                      @Override
                      public ParseQuery<ParseObject> create() {
                          //call the search function that returns a query of ParseObjects
                          // ParseQuery<ParseObject> query=
                          return null;
                      }
                  });*/
        List<ParseObject> testerList = new ArrayList<ParseObject>();
        testerList.add(new ParseObject("Listing"));
        ListingAdapter listAdapter = new ListingAdapter(this, testerList);



        // make a list view and configure it with the created adapter
          // create a ListView data structure to contain the adapter
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
        ListView displayedListing = (ListView) findViewById(R.id.listViewListing);

        // create a click listener and display the details of the Lsiting object when clicked
        displayedListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //make a ListingPopup object to show the clicked Listing
                ListingPopup popup = new ListingPopup(listingView.this ,   // Context
                        (ParseObject) parent.getAdapter().getItem(position) , //ParseObject to be displayed. How to get the ParseObject???????
                                                       viewClicked);   // View where popup will be shown
            }
        });


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
