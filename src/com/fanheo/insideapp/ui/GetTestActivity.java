package com.fanheo.insideapp.ui;
 
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.util.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
 
public class GetTestActivity extends Activity {
    private TextView textView; // ����textview
    private ProgressDialog pDialog;
    private TextView textView2; // ����textview����ʾ��ȡ����������
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testget);
        textView = (TextView) findViewById(R.id.text);
        textView2 = (TextView) findViewById(R.id.text2);
    }
    public void method1(View view) {
        pDialog = ProgressDialog.show(this, "���Ե�", "���ݼ�����");
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; // һ���@ȡ���׵�url��ַ
        HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
            public void onSuccess(String arg0) { // ��ȡ���ݳɹ����������
                pDialog.dismiss();
                textView.setText("��ȡjson���ݳɹ���������");
                textView2.setText(arg0);
                Log.i("hck", arg0);
            };
            public void onFailure(Throwable arg0) { // ʧ�ܣ�����
                Toast.makeText(GetTestActivity.this, "onFailure",
                        Toast.LENGTH_LONG).show();
            };
            public void onFinish() { // ��ɺ���ã�ʧ�ܣ��ɹ�����Ҫ��
            }
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			};
        });
    }
    public void method2(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?";
        RequestParams params = new RequestParams(); // �󶨲���
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray arg0) { // �ɹ��󷵻�һ��JSONArray����
                Log.i("hck", arg0.length() + "");
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONObject(2).getString("name")); //���ص���JSONArray�� ��ȡJSONArray��������ĵ�2��JSONObject����Ȼ���ȡ����Ϊname������ֵ
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
            public void onFailure(Throwable arg0) {
                Log.e("hck", " onFailure" + arg0.toString());
            };
            public void onFinish() {
                Log.i("hck", "onFinish");
            };
            public void onSuccess(JSONObject arg0) {   //���ص���JSONObject�����������
                Log.i("hck", "onSuccess ");
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONArray("list").getJSONObject(0)
                                    .getString("name"));
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
        });
    }
    public void method3(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10";
        HttpUtil.get(urlString, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject arg0) {
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONArray("list").getJSONObject(1)
                                    .getString("name"));
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
        });
    }
    public void method4(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?";
        final RequestParams params = new RequestParams();
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String arg0) {
                try {
                    JSONObject jObject = new JSONObject(arg0);
                    textView.setText("�������֣�"
                            + jObject.getJSONArray("list").getJSONObject(2)
                                    .getString("name"));
                    Log.i("hck", params.getEntity(null).toString());
                } catch (Exception e) {
                }
            }

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			};
        });
    }
}