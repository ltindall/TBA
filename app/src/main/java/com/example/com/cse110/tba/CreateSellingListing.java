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
        protected Button wCreateBuyingListingButton;

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
            wCreateBuyingListingButton = (Button)findViewById(R.id.createListingButton);

            wPrice = (EditText)findViewById(R.id.createListingBookPrice);
            wCondition = (EditText)findViewById(R.id.createListingBookCondition);
            wComment = (EditText)findViewById(R.id.createListingBookComment);
            wHardCover = (CheckBox)findViewById(R.id.createListingIsHardCover);


            // create listener for the create button
            wCreateBuyingListingButton.setOnClickListener(new View.OnClickListener() {
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
                    double bookEdition = Double.parseDouble(stringBookEdition);

                    String stringBookPrice = wBookYear.getText().toString().trim();
                    double bookPrice = Double.parseDouble(stringBookPrice);

                    String bookCondition = wCondition.getText().toString();
                    String bookComment = wComment.getText().toString();


                    // save it on parse as new Book object
                    ParseObject book = new ParseObject("CustomBook");
                    book.put("Title", bookTitle);
                    book.put("Author", bookAuthor);
                    book.put("ISBN", bookISBN);
                    book.put("Year", bookYear);
                    book.put("Edition", bookEdition);

                    // save it on parse as new Listing object
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String currentUserUsername = currentUser.getUsername();

                    ParseObject bookListing = new ParseObject("SellListing");
                    bookListing.put("Book", book)
                    bookListing.put("Price", bookPrice);
                    bookListing.put("Condition", bookCondition);
                    bookListing.put("Comment", bookComment);
                    bookListing.put("User", currentUser.getEmail());

                    boolean checked = wHardCover.isChecked();
                    if (checked) {
                        bookListing.put("HardCover", true);
                    }

                    else {
                        bookListing.put("HardCover", false);
                    }

                    // save it
                    book.saveInBackground();
                    bookListing.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // successfully storing everything
                                // create toast
                                Toast.makeText(CreateBuyingListing.this, "Sucees Creating Listing", Toast.LENGTH_LONG.show());

                                // bring user to the next page later (INTENT)
                            }

                            else {
                                // there is problem in storing
                                AlertDialog.Builder builder = new AlertDialog().Builder(CreateBuyingListing.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Ooops, something went wrong");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()) {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // close the dialogue message
                                        dialogInterface.dismiss();
                                    }
                                }
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
