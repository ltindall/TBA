package com.example.com.cse110.tba;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Joshua Lynch on 5/14/2015.
 * Creates a popup object that displays the data from a CustomBook object
 */
public class ListingPopup
{
    private ParseObject listing;
    private Context context;
    private PopupWindow popup;
    private int listType;
    private int isbn;
    private String title;
    private List<String> listingValues;

    public ListingPopup(Context c, ParseObject p, View parentView, boolean isMyListing, List<String> values, final int position)
    {
        listingValues = values;
        context = c;
        listing = p;
        isbn = listing.getParseObject("Book").getInt("ISBN");
        title = listing.getParseObject("Book").getString("Title");
        if(listing.getClassName().equals("BuyListing"))
        {
            listType = MarketHistory.BUY_HISTORY;
        }
        else
        {
            listType = MarketHistory.SELL_HISTORY;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popUpView;

        if (!isMyListing) {

            String email = ParseUser.getCurrentUser().getEmail();

            if (email != null)
                 popUpView = inflater.inflate(R.layout.listing_popup, null, false);
            else
                 popUpView = inflater.inflate(R.layout.anon_popup, null, false);

            popup = new PopupWindow(popUpView, 400, 580, true);
            popup.setContentView(popUpView);

            TextView title = (TextView) popUpView.findViewById(R.id.popup_title);
            title.setText("Title: " + listing.getParseObject("Book").getString("Title"));

            TextView author = (TextView) popUpView.findViewById(R.id.popup_author);
            author.setText("Author: " + listing.getParseObject("Book").getString("Author"));

            TextView isbn = (TextView) popUpView.findViewById(R.id.popup_isbn);
            isbn.setText("ISBN: " + listing.getParseObject("Book").getInt("ISBN"));

            TextView year = (TextView) popUpView.findViewById(R.id.popup_year);
            year.setText("Year: " + listing.getParseObject("Book").getInt("Year"));

            TextView edition = (TextView) popUpView.findViewById(R.id.popup_edition);
            edition.setText("Edition: " + listing.getParseObject("Book").getInt("Edition"));

            TextView price = (TextView) popUpView.findViewById(R.id.popup_price);
            price.setText("Price: " + listing.getDouble("Price"));

            if (listing.getInt("Condition") == 0) {
                TextView condition = (TextView) popUpView.findViewById(R.id.popup_condition);
                condition.setText("Condition: Old");
            } else {
                TextView condition = (TextView) popUpView.findViewById(R.id.popup_condition);
                condition.setText("Condition: New");
            }

            TextView comment = (TextView) popUpView.findViewById(R.id.popup_comment);
            comment.setText("Comment: " + listing.getString("Comment"));

            if (listing.getBoolean("HardCover")) {
                TextView hardcover = (TextView) popUpView.findViewById(R.id.popup_hardcover);
                hardcover.setText("Hardcover: Yes");
            } else {
                TextView hardcover = (TextView) popUpView.findViewById(R.id.popup_hardcover);
                hardcover.setText("Hardcover: No");
            }

            Button exit = (Button) popUpView.findViewById(R.id.popup_button);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                }
            });

            if (email != null) {
                Button contact = (Button) popUpView.findViewById(R.id.popup_contact);
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("notify user button", "is working");
                        DBManager.notifyUser(listing);
                    }
                });
            }

            Button history = (Button) popUpView.findViewById(R.id.popup_history);
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMarketHistory();
                    popup.dismiss();

                }
            });
        }

        else {
            popUpView = inflater.inflate(R.layout.my_listings_popup, null, false);

            popup = new PopupWindow(popUpView, 400, 580, true);
            popup.setContentView(popUpView);

            TextView title = (TextView) popUpView.findViewById(R.id.popup_title1);
            title.setText("Title: " + listing.getParseObject("Book").getString("Title"));

            TextView author = (TextView) popUpView.findViewById(R.id.popup_author1);
            author.setText("Author: " + listing.getParseObject("Book").getString("Author"));

            TextView isbn = (TextView) popUpView.findViewById(R.id.popup_isbn1);
            isbn.setText("ISBN: " + listing.getParseObject("Book").getInt("ISBN"));

            TextView year = (TextView) popUpView.findViewById(R.id.popup_year1);
            year.setText("Year: " + listing.getParseObject("Book").getInt("Year"));

            TextView edition = (TextView) popUpView.findViewById(R.id.popup_edition1);
            edition.setText("Edition: " + listing.getParseObject("Book").getInt("Edition"));

            TextView price = (TextView) popUpView.findViewById(R.id.popup_price1);
            price.setText("Price: " + listing.getDouble("Price"));

            if (listing.getInt("Condition") == 0) {
                TextView condition = (TextView) popUpView.findViewById(R.id.popup_condition1);
                condition.setText("Condition: Old");
            } else {
                TextView condition = (TextView) popUpView.findViewById(R.id.popup_condition1);
                condition.setText("Condition: New");
            }

            TextView comment = (TextView) popUpView.findViewById(R.id.popup_comment1);
            comment.setText("Comment: " + listing.getString("Comment"));

            if (listing.getBoolean("HardCover")) {
                TextView hardcover = (TextView) popUpView.findViewById(R.id.popup_hardcover1);
                hardcover.setText("Hardcover: Yes");
            } else {
                TextView hardcover = (TextView) popUpView.findViewById(R.id.popup_hardcover1);
                hardcover.setText("Hardcover: No");
            }

            Button exit = (Button) popUpView.findViewById(R.id.popup_button1);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.dismiss();
                }
            });

            Button sold = (Button) popUpView.findViewById(R.id.popup_soldButton);
            sold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listing.put("isHistory", true);
                    listingValues.remove(position);
                    listing.saveInBackground();
                    popup.dismiss();
                    //Log.d("History: ", "this is history: " + listing.get("isHistory"));
                }
            });

            Log.d("History: ", "this is history: " + listing.get("isHistory"));

            Button delete = (Button) popUpView.findViewById(R.id.popup_delete1);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //remove the listing
                    listingValues.remove(position);
                    listing.deleteInBackground();
                    Toast.makeText(context,
                            "Item Deleted", Toast.LENGTH_LONG)
                            .show();

                    popup.dismiss();
                }
            });


            Button history = (Button) popUpView.findViewById(R.id.popup_history1);
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMarketHistory();
                    popup.dismiss();

                }
            });
        }

        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setContentView(popUpView);
        popup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        Log.d("ListingPopup", "Popup created");

    }

    public void showMarketHistory()
    {
        Intent intent = new Intent(context , MarketHistory.class);
        intent.putExtra("listingType", listType);
        intent.putExtra("ISBN", isbn);
        intent.putExtra("Title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
