package com.example.com.cse110.tba;

import android.content.Context;
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

    public ListingPopup(Context c, ParseObject p, View parentView)
    {
        context = c;
        listing = p;

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

        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setContentView(popUpView);
        popup.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        Log.d("ListingPopup", "Popup created");

    }

    public void closePopup(View v)
    {
        popup.dismiss();
    }
}
