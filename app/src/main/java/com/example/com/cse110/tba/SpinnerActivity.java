package com.example.com.cse110.tba;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Alexander on 5/7/2015.
 */
public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView parent, View view, int position, long id){
        // An item was selected.  You can retrieve the selected item
        // parent.getItemAtPosition (pos)
    }

    public void onNothingSelected(AdapterView<?> parent){
        // Another interface callback
    }
}
