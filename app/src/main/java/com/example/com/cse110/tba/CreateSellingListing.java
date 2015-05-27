package com.example.com.cse110.tba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
/**
 * Created by Chandra on 5/10/15.
 */

public class CreateSellingListing extends Activity {

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

        // button information

        protected CheckBox wNewBook;
        protected CheckBox wUsedBook;
        protected Button wCreateSellingListingButton;

        private DBManager manager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_buying_listing);

            // initializing
            wBookTitle = (EditText)findViewById(R.id.createListingBookTitle);
            wBookAuthor = (EditText)findViewById(R.id.createListingBookAuthor);
            wBookISBN = (EditText)findViewById(R.id.createListingISBNNumber);
            wBookYear = (EditText)findViewById(R.id.createListingBookYear);
            wBookEdition = (EditText)findViewById(R.id.createListingBookEdition);
            wCreateSellingListingButton = (Button)findViewById(R.id.createListingButton);

            wPrice = (EditText)findViewById(R.id.createListingBookPrice);
            wCondition = (EditText)findViewById(R.id.createListingBookCondition);
            wComment = (EditText)findViewById(R.id.createListingBookComment);

            wNewBook = (CheckBox)findViewById(R.id.bookConditionNew);
            wUsedBook = (CheckBox)findViewById(R.id.bookConditionUsed);
            wHardCover = (CheckBox)findViewById(R.id.createListingIsHardCover);


            // create listener for the create button
            wCreateSellingListingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get all the book information and convert them into string
                    String bookTitle = wBookTitle.getText().toString().trim();
                    String bookAuthor = wBookAuthor.getText().toString().trim();

                    String stringBookISBN = wBookISBN.getText().toString().trim();
                    int bookISBN = Integer.parseInt(stringBookISBN);

                    String stringBookYear = wBookYear.getText().toString().trim();
                    int bookYear = Integer.parseInt(stringBookYear);

                    String stringBookEdition = wBookYear.getText().toString().trim();
                    int bookEdition = Integer.parseInt(stringBookEdition);

                    String stringBookPrice = wBookYear.getText().toString().trim();
                    float bookPrice = Float.parseFloat(stringBookPrice);

                    boolean checkNew = wNewBook.isChecked();
                    boolean checkUsed = wUsedBook.isChecked();

                    int newBookOrNot = 0;
                    if (checkNew) {
                        newBookOrNot = 1;
                    }

                    if (checkUsed) {
                        newBookOrNot = 0;
                    }

                    String bookComment = wComment.getText().toString();
                    boolean hardCoverChecked = wHardCover.isChecked();

                    manager.addBookListing(false, bookTitle, bookAuthor, bookISBN, bookPrice, newBookOrNot,
                            bookYear, bookEdition, bookComment, hardCoverChecked);

                    // save it on parse as new Book object
                    // save it on parse as new Listing object



                    // save it
                    book.saveInBackground();
                    bookListing.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // successfully storing everything
                                // create toast
                                Toast.makeText(CreateSellingListing.this, "Sucees Creating Listing", Toast.LENGTH_LONG);

                                // bring user to the next page later (INTENT)
                            }

                            else {
                                // there is problem in storing
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSellingListing.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Ooops, something went wrong");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // close the dialogue message
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                    });




                }
            });
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
}
