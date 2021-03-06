package com.fanheo.insideapp.ui;

import java.io.File;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.data.UserPreferences;
import com.fanheo.insideapp.impl.FileDownloader;
import com.fanheo.insideapp.impl.SmartDownloadProgressListener;
import com.fanheo.insideapp.impl.SmartFileDownloader;
import com.fanheo.insideapp.ui.MainTabActivity.MessageReceiver;
import com.fanheo.insideapp.util.CommonUtils;
import com.fanheo.insideapp.util.ExampleUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
	Toast mToast;
	private static final String TAG = "JPush";
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final View view = View.inflate(this, R.layout.activity_splash, null);
		super.onCreate(savedInstanceState);
		// 去掉标题栏和全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);

		String alias = "guodong";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(MainActivity.this, R.string.error_alias_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(MainActivity.this, R.string.error_tag_gs_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		registerMessageReceiver(); // used for receive msg

		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		view.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// 这里淡入动画结束后，进行sd卡，网络的检测，下版本加入语言初始化、
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sd card 可用
					isNetworkAlertDialog();
				} else {
					// 当前SD 卡不可用
					new AlertDialog.Builder(
							com.fanheo.insideapp.ui.MainActivity.this)
							.setCancelable(false)
							.setTitle("温馨提示")
							.setMessage("未发现SD卡，您确定要继续吗?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 与sdcard可用部分相同
											isNetworkAlertDialog();
										}
									})
							.setNegativeButton("取消",
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

	private void setAlias() {
		// 创建异步的客户端对象
		AsyncHttpClient client = new AsyncHttpClient();
		// 请求的地址
		String url = "http://fanheo.com/index.php/admin2-android-";
		// 创建请求参数的封装的对象
		RequestParams params = new RequestParams();
		// 发送get请求的时候 url地址 相应参数,匿名回调对象
		client.get(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// 成功处理的方法
				System.out
						.println("statusCode-------------------" + statusCode);
				// 遍历头信息
				for (int i = 0; i < headers.length; i++) {
					Header header = headers[i];
					System.out.println("header------------Name:"
							+ header.getName() + ",--Value:"
							+ header.getValue());
				}
				// 设置控件内容

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 失败处理的方法
				error.printStackTrace();
			}
		});
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;

			case MSG_SET_TAGS:
				Log.d(TAG, "Set tags in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_ALIAS, alias),
							1000 * 60);
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
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
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
	private String autoname;

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
	 * 跳转到下一个Activity
	 */
	public void toNextActivity() {
		UserPreferences preferences = new UserPreferences();
		preferences.init(MainActivity.this);
		if (preferences.getAutoLogin()) {
			autoname = preferences.getName();
			String autopassword = preferences.getPWD();
			loginByAsyncHttpClientPost(autoname, autopassword);
		} else {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 采用AsyncHttpClient的Post方式进行实现
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByAsyncHttpClientPost(String userName, String userPass) {
		AsyncHttpClient client = new AsyncHttpClient(); // 创建异步请求的客户端对象
		String url = "http://fanheo.com/index.php/admin2-AndroidLogin-login"; // 定义请求的地址
		// 创建请求参数的封装的对象
		RequestParams params = new RequestParams();
		params.put("user_name", userName); // 设置请求的参数名和参数值
		params.put("user_pass", userPass);// 设置请求的参数名和参数
		// 执行post方法
	    CookieStore cookieStore = new PersistentCookieStore(this);  
        client.setCookieStore(cookieStore);  
        HttpContext httpContext = client.getHttpContext();  
        CookieStore cookies = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);  
        if(cookies!=null){  
            for(Cookie c:cookies.getCookies()){  
                Log.d("login activtity onsuccess before ~~"+c.getName(),c.getValue());  
            }  
        }else{  
            Log.d("login activtity onsuccess  before~~","cookies is null");  
        }  
		client.post(url, params, new AsyncHttpResponseHandler() {
			/**
			 * 成功处理的方法 statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器 ;
			 * responseBody:响应内容的字节
			 */
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200) {
					ShowToast(R.string.login_success);
					setAliasByAsyncHttpClientPost(autoname);
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ListViewActivity.class);
					startActivity(intent);
					finish();
				} else {
					ShowToast(R.string.login_false);
					System.out.println("test");
				}
			}

			private void setAliasByAsyncHttpClientPost(String name) {
				AsyncHttpClient client = new AsyncHttpClient(); // 创建异步请求的客户端对象
				String url = "http://fanheo.com/index.php/admin2-AndroidLogin-login"; // 定义请求的地址
				// 创建请求参数的封装的对象
				RequestParams params = new RequestParams();
				params.put("aliasname", name); // 设置请求的参数名和参数值

				// 执行post方法
				client.post(url, params, new AsyncHttpResponseHandler() {
					/**
					 * 成功处理的方法 statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器
					 * ; responseBody:响应内容的字节
					 */
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						if (statusCode == 200) {
							// 遍历头信息
							for (int i = 0; i < headers.length; i++) {
								Header header = headers[i];
								System.out.println("header------------Name:"
										+ header.getName() + ",--Value:"
										+ header.getValue());
							}
							String alias = "guodong";
							if (TextUtils.isEmpty(alias)) {
								Toast.makeText(MainActivity.this,
										R.string.error_alias_empty,
										Toast.LENGTH_SHORT).show();
								return;
							}
							if (!ExampleUtil.isValidTagAndAlias(alias)) {
								Toast.makeText(MainActivity.this,
										R.string.error_tag_gs_empty,
										Toast.LENGTH_SHORT).show();
								return;
							}
							mHandler.sendMessage(mHandler.obtainMessage(
									MSG_SET_ALIAS, alias));
						} else {
							System.out.println("test");
						}
					}

					private final Handler mHandler = new Handler() {
						@SuppressWarnings("unchecked")
						@SuppressLint("HandlerLeak")
						@Override
						public void handleMessage(android.os.Message msg) {
							super.handleMessage(msg);
							switch (msg.what) {
							case MSG_SET_ALIAS:
								Log.d(TAG, "Set alias in handler.");
								JPushInterface.setAliasAndTags(
										getApplicationContext(),
										(String) msg.obj, null, mAliasCallback);
								break;

							case MSG_SET_TAGS:
								Log.d(TAG, "Set tags in handler.");
								JPushInterface.setAliasAndTags(
										getApplicationContext(), null,
										(Set<String>) msg.obj, mTagsCallback);
								break;

							default:
								Log.i(TAG, "Unhandled msg - " + msg.what);
							}
						}
					};
					private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
								Set<String> tags) {
							String logs;
							switch (code) {
							case 0:
								logs = "Set tag and alias success";
								Log.i(TAG, logs);
								break;

							case 6002:
								logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
								Log.i(TAG, logs);
								if (ExampleUtil
										.isConnected(getApplicationContext())) {
									mHandler.sendMessageDelayed(
											mHandler.obtainMessage(
													MSG_SET_ALIAS, alias),
											1000 * 60);
								} else {
									Log.i(TAG, "No network");
								}
								break;

							default:
								logs = "Failed with errorCode = " + code;
								Log.e(TAG, logs);
							}

							ExampleUtil
									.showToast(logs, getApplicationContext());
						}

					};
					private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
								Set<String> tags) {
							String logs;
							switch (code) {
							case 0:
								logs = "Set tag and alias success";
								Log.i(TAG, logs);
								break;

							case 6002:
								logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
								Log.i(TAG, logs);
								if (ExampleUtil
										.isConnected(getApplicationContext())) {
									mHandler.sendMessageDelayed(mHandler
											.obtainMessage(MSG_SET_TAGS, tags),
											1000 * 60);
								} else {
									Log.i(TAG, "No network");
								}
								break;

							default:
								logs = "Failed with errorCode = " + code;
								Log.e(TAG, logs);
							}

							ExampleUtil
									.showToast(logs, getApplicationContext());
						}

					};

					/**
					 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
					 */
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// ShowToast(R.string.login_false);
						error.printStackTrace();// 把错误信息打印出轨迹来
					}
				});

			}

			/**
			 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
			 */
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				ShowToast(R.string.login_false);
				error.printStackTrace();// 把错误信息打印出轨迹来
			}
		});
	}

	/**
	 * Toast
	 * 
	 * @param resId
	 */
	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(
							MainActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/**
	 * 判断网络是否可用
	 * 
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
	 * 判断网络是否可用弹出对话框
	 */
	public void isNetworkAlertDialog() {
		if (isNetworkAvailable(com.fanheo.insideapp.ui.MainActivity.this) == true) {
			/*
			 * String path =
			 * "http://fanheo.com:88/index.php/home-index-index.html";
			 * if(Environment
			 * .getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			 * File dir = Environment.getExternalStorageDirectory();//文件保存目录
			 * download(path, dir); }else{ Toast.makeText(MainActivity.this,
			 * R.string.sdcarderror, 1).show(); }
			 */
			Toast.makeText(MainActivity.this, "存在网络", Toast.LENGTH_LONG).show();
			// 跳转Activity，判断是否第一次运行
			toNextActivity();
		} else {
			new AlertDialog.Builder(com.fanheo.insideapp.ui.MainActivity.this)
					.setCancelable(false)
					.setTitle("温馨提示")
					.setMessage("未发现网络，您确定要继续吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									toNextActivity();
								}
							})
					.setNegativeButton("设置网络",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									startNetworkSettingActivity(com.fanheo.insideapp.ui.MainActivity.this);
								}
							}).show();

		}
	}

	/**
	 * 跳转到设置网络界面
	 * 
	 * @param context
	 */

	public static void startNetworkSettingActivity(Context context) {
		Intent intent = new Intent();
		final int sdkVersion = VERSION.SDK_INT;
		if (sdkVersion >= 11) {
			intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		} else {
			intent.setClassName("com.android.settings",
					"com.android.settings.WirelessSettings");// android4.0系统找不到此activity。
		}
		context.startActivity(intent);
	}

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void init() {
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

	// for receive customer msg from jpush server
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

	private void setCostomMsg(String msg) {

	}

}
