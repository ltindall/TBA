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

import com.parse.ParseObject;

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

    public ListingPopup(Context c, ParseObject p, View parentView)
    {
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
        View popUpView = inflater.inflate(R.layout.listing_popup, null, false);
        popup = new PopupWindow(popUpView, 400, 580, true);
        popup.setContentView(popUpView);

        TextView title = (TextView)popUpView.findViewById(R.id.popup_title);
        title.setText("Title: " + listing.getParseObject("Book").getString("Title"));

        TextView isbn = (TextView)popUpView.findViewById(R.id.popup_isbn);
        isbn.setText("ISBN: " + listing.getParseObject("Book").getInt("ISBN"));

        TextView price = (TextView)popUpView.findViewById(R.id.popup_price);
        price.setText("Price: " + listing.getDouble("Price"));


        Button exit = (Button)popUpView.findViewById(R.id.popup_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

        Button history = (Button)popUpView.findViewById(R.id.popup_history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMarketHistory();
                popup.dismiss();
            }
        });

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
