package com.example.com.cse110.tba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.Parse;
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

    /*
        A getter function to access the List of ParseObjects
     */
    public List<ParseObject> getmListing() {
        return mListing;
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
            holder.bookConditionListingAdapter = (TextView) convertView
                    .findViewById(R.id.bookListingBookCondition);
            holder.bookUserListingAdapter = (TextView) convertView
                    .findViewById(R.id.bookListingBookOwner);

            convertView.setTag(holder);
        }

        else {

            holder = (ViewHolder) convertView.getTag();

        }

        ParseObject listingObject = mListing.get(position);

        // get the book first
        ParseObject bookObject = (ParseObject)listingObject.get("Book");

        // book Title
        String bookTitle = bookObject.getString("Title");
        holder.bookTitleListingAdapter.setText(bookTitle);

        // book ISBN
        String bookISBN = bookObject.getString("ISBN");
        holder.bookISBNListingAdapter.setText(bookISBN);

        // book Condition
        String bookCondition = listingObject.getString("ISBN");
        holder.bookISBNListingAdapter.setText(bookCondition);

        // book Price
        String bookPrice = listingObject.getString("Condition");
        holder.bookISBNListingAdapter.setText(bookPrice);

        // book User
        String bookUser = listingObject.getString("User");
        holder.bookISBNListingAdapter.setText(bookUser);

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
