package com.example.com.cse110.tba;

import com.parse.Parse;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "9Sejg1tgXT9qRkeS3uqoykI9E84kVb8DAlCPzsNi", "MtMjrzfToRzCGf7mzFtnPXlApnUFBAqEGPmty8bm");
		ParseObject testObject = new ParseObject("test");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
