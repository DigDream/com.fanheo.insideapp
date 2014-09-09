package com.fanheo.insideapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fanheo.insideapp.CommonUtils;

/**
 * @ClassName: LoginActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-3 下午4:41:42
 */
public class LoginActivity extends Activity implements OnClickListener {
	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
	}

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

	Toast mToast;

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
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

	private void login() {
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();

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
		String httpUrl = "http://10.0.2.2:8089/Gossip/LoginServlet";
		System.out.println(httpUrl);
		HttpPost request = new HttpPost(httpUrl);
		HttpClient httpClient = new DefaultHttpClient();
		// 传递参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", name));
		params.add(new BasicNameValuePair("password", password));
		HttpResponse response;
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			request.setEntity(entity);
			response = httpClient.execute(request);

			// 如果返回状态为200，获得返回的结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = EntityUtils.toString(response.getEntity());
				System.out.println("response:" + str);
				if (str.trim().equals("success")) {
					// 用户登录成功
					System.out.println("登录成功！");
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
				} else {
					// 用户登录失败
					System.out.println("登录失败！");
				}
			} else {
				System.out.println("连接失败！");
			}
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
