package com.fanheo.insideapp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.fanheo.insideapp.data.UserPreferences;
import com.fanheo.insideapp.util.CommonUtils;
import com.fanheo.insideapp.util.ExampleUtil;
import com.fanheo.insideapp.R;
import com.fanheo.insideapp.UpdateManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity implements OnClickListener {
	EditText et_username, et_password;
	Button btn_login;
	Toast mToast;
	private String name;
	private String password;
	private static final String TAG = "JPush";
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 显示界面
		init();
		
		//auto_login();
		// 显示
		//initData();
		//UpdateManager manager = new UpdateManager(LoginActivity.this);
		// 检查软件更新
		//manager.checkUpdate();
	}

	private void auto_login() {
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if (!isNetConnected) {
			ShowToast(R.string.network_tips);
			return;
		}
		UserPreferences preferences = new UserPreferences();
		preferences.init(LoginActivity.this);
		if (preferences.getAutoLogin()) {
			String autoname = preferences.getName();
			String autopassword = preferences.getPWD();
			loginByAsyncHttpClientPost(autoname, autopassword);
		}
	}

	/**
	 * findViewById
	 */
	private void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);

	}

	/**
	 * 点击事件
	 */

	@Override
	public void onClick(View v) {
		if (v == btn_login) {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if (!isNetConnected) {
				ShowToast(R.string.network_tips);
				return;
			}
			login();
		}
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
				if (mToast == null) {
					mToast = Toast.makeText(
							LoginActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/**
	 * 登录函数
	 */
	private void login() {
		name = et_username.getText().toString();
		password = et_password.getText().toString();

		if (TextUtils.isEmpty(name)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}

		final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("正在登陆...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// post方法
		loginByAsyncHttpClientPost(name, password);
		
		progress.dismiss();
		/*
		 * String httpUrl = "http://10.0.2.2:8089/Gossip/LoginServlet";
		 * System.out.println(httpUrl); HttpPost request = new
		 * HttpPost(httpUrl); HttpClient httpClient = new DefaultHttpClient();
		 * // 传递参数 List<NameValuePair> params = new ArrayList<NameValuePair>();
		 * params.add(new BasicNameValuePair("username", name)); params.add(new
		 * BasicNameValuePair("password", password)); HttpResponse response; try
		 * { HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		 * request.setEntity(entity); response = httpClient.execute(request);
		 * 
		 * // 如果返回状态为200，获得返回的结果 if (response.getStatusLine().getStatusCode() ==
		 * HttpStatus.SC_OK) { String str =
		 * EntityUtils.toString(response.getEntity());
		 * System.out.println("response:" + str); if
		 * (str.trim().equals("success")) { // 用户登录成功
		 * System.out.println("登录成功！"); Intent intent = new
		 * Intent(LoginActivity.this, MainActivity.class);
		 * startActivity(intent); } else { // 用户登录失败
		 * System.out.println("登录失败！"); } } else { System.out.println("连接失败！");
		 * } } catch (ClientProtocolException e) {
		 * 
		 * e.printStackTrace(); } catch (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
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
		client.post(url, params, new AsyncHttpResponseHandler() {
			/**
			 * 成功处理的方法 statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器 ;
			 * responseBody:响应内容的字节
			 */
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200) {
					UserPreferences preferences = new UserPreferences();
					preferences.init(LoginActivity.this);
					preferences.saveName(name);
					preferences.savePWD(password);
					// preferences设置自动登录。。
					preferences.setAutoLogin(true);
					setAliasByAsyncHttpClientPost(name);

					ShowToast(R.string.login_success);
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, ListViewActivity.class);
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
					 * 成功处理的方法 statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器 ;
					 * responseBody:响应内容的字节
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
								Toast.makeText(LoginActivity.this, R.string.error_alias_empty,
										Toast.LENGTH_SHORT).show();
								return;
							}
							if (!ExampleUtil.isValidTagAndAlias(alias)) {
								Toast.makeText(LoginActivity.this, R.string.error_tag_gs_empty,
										Toast.LENGTH_SHORT).show();
								return;
							}
							mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
						} else {
							ShowToast(R.string.login_false);
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



					/**
					 * 失败处理的方法 error：响应失败的错误信息封装到这个异常对象中
					 */
					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
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
	 * 采用AsyncHttpClient的Get方式进行实现
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByAsyncHttpClientGet(String userName, String userPass) {
		// 创建异步的客户端对象
		AsyncHttpClient client = new AsyncHttpClient();
		// 请求的地址
		String url = "http://test.com";
		// 创建请求参数的封装的对象
		RequestParams params = new RequestParams();
		params.put("user_name", userName); // 设置请求的参数名和参数值
		params.put("user_pass", userPass);// 设置请求的参数名和参数

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

	/**
	 * 测试json解析。
	 */
	private void initData() {
		// 访问服务器端 获取json数据
		// 创建客户端对象
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://fanheo.com/index.php/admin2-AndroidLogin-login";
		client.get(url, new JsonHttpResponseHandler() {
			// 返回JSONArray对象 | JSONObject对象
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {

					// 存储数组变量
					List<String> objects = new ArrayList<>();
					for (int i = 0; i < response.length(); i++) {
						try {
							// 获取具体的一个JSONObject对象
							JSONObject obj = response.getJSONObject(i);
							objects.add(obj.getString("result"));
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				}
			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
