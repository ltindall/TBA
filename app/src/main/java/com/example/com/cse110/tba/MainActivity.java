package com.example.com.cse110.tba;

import com.parse.ParseObject;
import com.parse.ui.ParseLoginBuilder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	ParseObject testObject = new ParseObject("UserClass");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		testObject.put("NameField", "Lamp");
        testObject.add("commit", null);
        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), 0);
        //additional code here
		//testObject.saveInBackground();
		///push this
        //extra crap
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
