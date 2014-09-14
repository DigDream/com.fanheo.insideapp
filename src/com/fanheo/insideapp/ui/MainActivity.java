package com.fanheo.insideapp.ui;

import cn.jpush.android.api.JPushInterface;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.ui.MainTabActivity.MessageReceiver;
import com.fanheo.insideapp.util.ExampleUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static boolean isForeground = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final View view = View.inflate(this, R.layout.activity_splash, null);
		super.onCreate(savedInstanceState);
		// ȥ����������ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);
		registerMessageReceiver();  // used for receive msg

		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		view.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// ���ﵭ�붯�������󣬽���sd��������ļ�⣬�°汾�������Գ�ʼ����
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sd card ����
					isNetworkAlertDialog();
				} else {
					// ��ǰSD ��������
					new AlertDialog.Builder(
							com.fanheo.insideapp.ui.MainActivity.this)
							.setCancelable(false)
							.setTitle("��ܰ��ʾ")
							.setMessage("δ����SD������ȷ��Ҫ������?")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// ��sdcard���ò�����ͬ
											isNetworkAlertDialog();
										}
									})
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
											com.fanheo.insideapp.ui.MainActivity.this
													.stopService(getIntent());
											System.exit(0);
										}
									}).show();
				}
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationStart(Animation arg0) {

			}

		});

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

	/**
	 * ��ת����һ��Activity
	 */
	public void toNextActivity() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * �ж������Ƿ����
	 * @param context
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * �ж������Ƿ���õ����Ի���
	 */
	public void isNetworkAlertDialog() {
		if (isNetworkAvailable(com.fanheo.insideapp.ui.MainActivity.this) == true) {
			Toast.makeText(MainActivity.this, "��������", Toast.LENGTH_LONG).show();
			// ��תActivity���ж��Ƿ��һ������
			toNextActivity();
		} else {
			new AlertDialog.Builder(com.fanheo.insideapp.ui.MainActivity.this)
					.setCancelable(false)
					.setTitle("��ܰ��ʾ")
					.setMessage("δ�������磬��ȷ��Ҫ������?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									toNextActivity();
								}
							})
					.setNegativeButton("��������",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									startNetworkSettingActivity(com.fanheo.insideapp.ui.MainActivity.this);
								}
							}).show();

		}
	}
	/**
	 * ��ת�������������
	 * @param context
	 */

	public static void startNetworkSettingActivity(Context context) {
		Intent intent = new Intent();
		final int sdkVersion = VERSION.SDK_INT;
		if (sdkVersion >= 11) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent.setClassName("com.android.settings",
					"com.android.settings.WirelessSettings");// android4.0ϵͳ�Ҳ�����activity��
		}
		context.startActivity(intent);
	}
	// ��ʼ�� JPush������Ѿ���ʼ������û�е�¼�ɹ�����ִ�����µ�¼��
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
			public static final String MESSAGE_RECEIVED_ACTION = "com.fanheo.insideapp.MESSAGE_RECEIVED_ACTION";
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
			

}
