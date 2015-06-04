package com.example.com.cse110.tba;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by Chandra on 5/16/15.
 */
public class ListingAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mListing;


    public ListingAdapter(Context context, List listing) {
        super(context, R.layout.listing_custom_layout, listing);
        mContext = context;
        mListing = listing;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listing_custom_layout, null);
            holder = new ViewHolder();
            holder.bookTitleListingAdapter = (TextView) convertView
                    .findViewById(R.id.bookListingBookTitle);
            holder.bookISBNListingAdapter = (TextView) convertView
                    .findViewById(R.id.bookListingBookISBN);
            holder.bookPriceListingAdapter = (TextView) convertView
                    .findViewById(R.id.bookListingBookPrice);

            convertView.setTag(holder);
        }

        else {

            holder = (ViewHolder) convertView.getTag();

        }

        ParseObject listingObject = mListing.get(position);

        // get the book first
        ParseObject bookObject = null;
        try {
            bookObject = listingObject.getParseObject("Book").fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // book Title
        String bookTitle = bookObject.getString("Title");
        holder.bookTitleListingAdapter.setText(bookTitle);


        // book ISBN
        String bookISBN = bookObject.getString("ISBN");
        holder.bookISBNListingAdapter.setText(bookISBN);

        // book Price
        String bookPrice = listingObject.getString("Price");
        holder.bookPriceListingAdapter.setText(bookPrice);

        return convertView;
    }

    public static class ViewHolder {
        TextView bookTitleListingAdapter;
        TextView bookISBNListingAdapter;
        TextView bookConditionListingAdapter;
        TextView bookPriceListingAdapter;
        TextView bookUserListingAdapter;

    }

}
