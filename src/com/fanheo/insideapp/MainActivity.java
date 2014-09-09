package com.fanheo.insideapp;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fanheo.insideapp.ExampleUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends InstrumentedActivity {

	public static boolean isForeground = false;
	private static final String TAG = "JPush";
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String alias = "test";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(MainActivity.this,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(MainActivity.this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerMessageReceiver();  // used for receive msg
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
	 private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(android.os.Message msg) {
	            super.handleMessage(msg);
	            switch (msg.what) {
	            case MSG_SET_ALIAS:
	                Log.d(TAG, "Set alias in handler.");
	                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
	                break;
	                
	            case MSG_SET_TAGS:
	                Log.d(TAG, "Set tags in handler.");
	                JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
	                break;
	                
	            default:
	                Log.i(TAG, "Unhandled msg - " + msg.what);
	            }
	        }
	    };
	    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

	        @Override
	        public void gotResult(int code, String alias, Set<String> tags) {
	            String logs ;
	            switch (code) {
	            case 0:
	                logs = "Set tag and alias success";
	                Log.i(TAG, logs);
	                break;
	                
	            case 6002:
	                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
	                Log.i(TAG, logs);
	                if (ExampleUtil.isConnected(getApplicationContext())) {
	                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
	                } else {
	                	Log.i(TAG, "No network");
	                }
	                break;
	            
	            default:
	                logs = "Failed with errorCode = " + code;
	                Log.e(TAG, logs);
	            }
	            
	            ExampleUtil.showToast(logs, getApplicationContext());
	        }
		    
		};
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(TAG, logs);
                break;
                
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(TAG, logs);
                if (ExampleUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                } else {
                	Log.i(TAG, "No network");
                }
                break;
            
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(TAG, logs);
            }
            
            ExampleUtil.showToast(logs, getApplicationContext());
        }
        
    };
		
	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
		private void init(){
			 JPushInterface.init(getApplicationContext());
		}


		@Override
		protected void onResume() {
			isForeground = true;
			super.onResume();
		}


		@Override
		protected void onPause() {
			isForeground = false;
			super.onPause();
		}


		@Override
		protected void onDestroy() {
			unregisterReceiver(mMessageReceiver);
			super.onDestroy();
		}
		
	
	//for receive customer msg from jpush server
		private MessageReceiver mMessageReceiver;
		public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
		public static final String KEY_TITLE = "title";
		public static final String KEY_MESSAGE = "message";
		public static final String KEY_EXTRAS = "extras";
		
		public void registerMessageReceiver() {
			mMessageReceiver = new MessageReceiver();
			IntentFilter filter = new IntentFilter();
			filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
			filter.addAction(MESSAGE_RECEIVED_ACTION);
			registerReceiver(mMessageReceiver, filter);
		}

		public class MessageReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
	              String messge = intent.getStringExtra(KEY_MESSAGE);
	              String extras = intent.getStringExtra(KEY_EXTRAS);
	              StringBuilder showMsg = new StringBuilder();
	              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
	              if (!ExampleUtil.isEmpty(extras)) {
	            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
	              }
	              setCostomMsg(showMsg.toString());
				}
			}
		}
		
		private void setCostomMsg(String msg){
			
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
