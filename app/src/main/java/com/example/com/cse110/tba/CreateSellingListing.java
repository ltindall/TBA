package com.example.com.cse110.tba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Chandra on 5/10/15.
 */

public class CreateSellingListing extends Activity implements DBAsync {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_selling_listing);*/

    // book information
    protected EditText wBookTitle;
    protected EditText wBookAuthor;
    protected EditText wBookISBN;
    protected EditText wBookYear;
    protected EditText wBookEdition;

    protected EditText wPrice;
    protected EditText wCondition;
    protected EditText wComment;
    protected CheckBox wHardCover;

    boolean checkNew = false;
    boolean checkUsed = false;
    boolean isHardCover = false;
    // button information

    protected CheckBox wNewBook;
    protected CheckBox wUsedBook;
    protected Button wCreateSellingListingButton;

    private DBManager manager = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_selling_listing);

        // initializing
        wBookTitle = (EditText) findViewById(R.id.createSellListingBookTitle);
        wBookAuthor = (EditText) findViewById(R.id.createSellListingBookAuthor);
        wBookISBN = (EditText) findViewById(R.id.createSellListingISBNNumber);
        wBookYear = (EditText) findViewById(R.id.createSellListingBookYear);
        wBookEdition = (EditText) findViewById(R.id.createSellListingBookEdition);
        wCreateSellingListingButton = (Button) findViewById(R.id.createSellListingButton);

        wPrice = (EditText) findViewById(R.id.createSellListingBookPrice);
        wComment = (EditText) findViewById(R.id.createSellListingBookComment);

        wNewBook = (CheckBox) findViewById(R.id.bookConditionNew);
        wUsedBook = (CheckBox) findViewById(R.id.bookConditionUsed);
        wHardCover = (CheckBox) findViewById(R.id.createListingIsHardCover);

        wNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checkNew = true;
                }
            }
        });

        wUsedBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checkUsed = true;
                }
            }
        });

        wHardCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    isHardCover = true;
                }
            }
        });


        // create listener for the create button
        wCreateSellingListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all the book information and convert them into string
                String bookTitle = wBookTitle.getText().toString().trim();
                String bookAuthor = wBookAuthor.getText().toString().trim();

                String stringBookISBN = wBookISBN.getText().toString().trim();
                long bookISBN = Long.parseLong(stringBookISBN);

                String stringBookYear = wBookYear.getText().toString().trim();
                int bookYear = Integer.parseInt(stringBookYear);

                String stringBookEdition = wBookYear.getText().toString().trim();
                int bookEdition = Integer.parseInt(stringBookEdition);

                String stringBookPrice = wBookYear.getText().toString().trim();
                float bookPrice = Float.parseFloat(stringBookPrice);


                int newBookOrNot = 0;
                if (checkNew) {
                    newBookOrNot = 1;
                }

                if (checkUsed) {
                    newBookOrNot = 0;
                }

                String bookComment = wComment.getText().toString();

                manager.addBookListing(false, bookTitle, bookAuthor, bookISBN, bookPrice, newBookOrNot,
                        bookYear, bookEdition, bookComment, isHardCover);

                // save it on parse as new Book object
                // save it on parse as new Listing object

                // Make a toast to signal that it's ok
                Toast.makeText(CreateSellingListing.this, "Success Creating Listing", Toast.LENGTH_LONG).show();

                finish();

                // save it
            }


        });

        //public void sendMessage(View view) {

        //}


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_selling_listing, menu);
        return true;
    }

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

    @Override
    public void onBuyListingsLoad(List<ParseObject> buyListings) {

    }

    @Override
    public void onSellListingsLoad(List<ParseObject> sellListings) {

    }

    @Override
    public void onBuyHistoryLoad(List<ParseObject> buyHistory) {

    }

    @Override
    public void onSellHistoryLoad(List<ParseObject> sellHistory) {

    }

    @Override
    public void onUserLoad(List<ParseUser> userList) {

    }
}
