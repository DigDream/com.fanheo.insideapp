package com.fanheo.insideapp.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanheo.insideapp.data.UserPreferences;
import com.fanheo.insideapp.util.CommonUtils;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// ��ʾ����
		init();
		
		//auto_login();
		// ��ʾ
		initData();
		UpdateManager manager = new UpdateManager(LoginActivity.this);
		// ����������
		manager.checkUpdate();
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
	 * ����¼�
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

	/**
	 * ��¼����
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
		progress.setMessage("���ڵ�½...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// post����
		loginByAsyncHttpClientPost(name, password);
		progress.dismiss();
		/*
		 * String httpUrl = "http://10.0.2.2:8089/Gossip/LoginServlet";
		 * System.out.println(httpUrl); HttpPost request = new
		 * HttpPost(httpUrl); HttpClient httpClient = new DefaultHttpClient();
		 * // ���ݲ��� List<NameValuePair> params = new ArrayList<NameValuePair>();
		 * params.add(new BasicNameValuePair("username", name)); params.add(new
		 * BasicNameValuePair("password", password)); HttpResponse response; try
		 * { HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		 * request.setEntity(entity); response = httpClient.execute(request);
		 * 
		 * // �������״̬Ϊ200����÷��صĽ�� if (response.getStatusLine().getStatusCode() ==
		 * HttpStatus.SC_OK) { String str =
		 * EntityUtils.toString(response.getEntity());
		 * System.out.println("response:" + str); if
		 * (str.trim().equals("success")) { // �û���¼�ɹ�
		 * System.out.println("��¼�ɹ���"); Intent intent = new
		 * Intent(LoginActivity.this, MainActivity.class);
		 * startActivity(intent); } else { // �û���¼ʧ��
		 * System.out.println("��¼ʧ�ܣ�"); } } else { System.out.println("����ʧ�ܣ�");
		 * } } catch (ClientProtocolException e) {
		 * 
		 * e.printStackTrace(); } catch (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * ����AsyncHttpClient��Post��ʽ����ʵ��
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByAsyncHttpClientPost(String userName, String userPass) {
		AsyncHttpClient client = new AsyncHttpClient(); // �����첽����Ŀͻ��˶���
		String url = "http://192.168.1.199/mao10cms/index.php?m=Admin2&c=AndroidLogin&a=login"; // ��������ĵ�ַ
		// ������������ķ�װ�Ķ���
		RequestParams params = new RequestParams();
		params.put("user_name", userName); // ��������Ĳ������Ͳ���ֵ
		params.put("user_pass", userPass);// ��������Ĳ������Ͳ���
		// ִ��post����
		client.post(url, params, new AsyncHttpResponseHandler() {
			/**
			 * �ɹ�����ķ��� statusCode:��Ӧ��״̬��; headers:��Ӧ��ͷ��Ϣ ���� ��Ӧ��ʱ�䣬��Ӧ�ķ����� ;
			 * responseBody:��Ӧ���ݵ��ֽ�
			 */
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200) {
					UserPreferences preferences = new UserPreferences();
					preferences.init(LoginActivity.this);
					preferences.saveName(name);
					preferences.savePWD(password);
					// preferences�����Զ���¼����
					preferences.setAutoLogin(true);

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

			/**
			 * ʧ�ܴ���ķ��� error����Ӧʧ�ܵĴ�����Ϣ��װ������쳣������
			 */
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				ShowToast(R.string.login_false);
				error.printStackTrace();// �Ѵ�����Ϣ��ӡ���켣��
			}
		});
	}

	/**
	 * ����AsyncHttpClient��Get��ʽ����ʵ��
	 * 
	 * @param userName
	 * @param userPass
	 */
	public void loginByAsyncHttpClientGet(String userName, String userPass) {
		// �����첽�Ŀͻ��˶���
		AsyncHttpClient client = new AsyncHttpClient();
		// ����ĵ�ַ
		String url = "http://test.com";
		// ������������ķ�װ�Ķ���
		RequestParams params = new RequestParams();
		params.put("user_name", userName); // ��������Ĳ������Ͳ���ֵ
		params.put("user_pass", userPass);// ��������Ĳ������Ͳ���

		// ����get�����ʱ�� url��ַ ��Ӧ����,�����ص�����
		client.get(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// �ɹ�����ķ���
				System.out
						.println("statusCode-------------------" + statusCode);
				// ����ͷ��Ϣ
				for (int i = 0; i < headers.length; i++) {
					Header header = headers[i];
					System.out.println("header------------Name:"
							+ header.getName() + ",--Value:"
							+ header.getValue());
				}
				// ���ÿؼ�����

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// ʧ�ܴ���ķ���
				error.printStackTrace();
			}
		});
	}

	/**
	 * ����json������
	 */
	private void initData() {
		// ���ʷ������� ��ȡjson����
		// �����ͻ��˶���
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://192.168.1.104/fanheo_home/index.php/Admin2-AndroidLogin-Login.html";
		client.get(url, new JsonHttpResponseHandler() {
			// ����JSONArray���� | JSONObject����
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {

					// �洢�������
					List<String> objects = new ArrayList<>();
					for (int i = 0; i < response.length(); i++) {
						try {
							// ��ȡ�����һ��JSONObject����
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
