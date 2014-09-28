package com.fanheo.insideapp.ui;

import com.fanheo.insideapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderInfoActivity extends Activity {

	private TextView tv_about;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderinfo);
		Bundle bundle=getIntent().getExtras();
		String name=bundle.getString("name");
		//int name=bundle.getInt("itemInfo");
		tv_about = (TextView)findViewById(R.id.about_version);
		tv_about.setText(name);
	}

}
