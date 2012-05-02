package com.nick.android.rcflyinglocations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private ImageView button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		button1 = (ImageView) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toMap = new Intent(MainActivity.this, MapsActivity.class);
				startActivity(toMap);
			}
			
		});
	}
	
	
	

}
