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
    private TextView textView; // 顶部textview
    private ProgressDialog pDialog;
    private TextView textView2; // 下面textview，显示获取的所有数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testget);
        textView = (TextView) findViewById(R.id.text);
        textView2 = (TextView) findViewById(R.id.text2);
    }
    public void method1(View view) {
        pDialog = ProgressDialog.show(this, "请稍等", "数据加载中");
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; // 一個獲取菜谱的url地址
        HttpUtil.get(urlString, new AsyncHttpResponseHandler() {
            public void onSuccess(String arg0) { // 获取数据成功会调用这里
                pDialog.dismiss();
                textView.setText("获取json数据成功，看下面");
                textView2.setText(arg0);
                Log.i("hck", arg0);
            };
            public void onFailure(Throwable arg0) { // 失败，调用
                Toast.makeText(GetTestActivity.this, "onFailure",
                        Toast.LENGTH_LONG).show();
            };
            public void onFinish() { // 完成后调用，失败，成功，都要掉
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
        RequestParams params = new RequestParams(); // 绑定参数
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray arg0) { // 成功后返回一个JSONArray数据
                Log.i("hck", arg0.length() + "");
                try {
                    textView.setText("菜谱名字："
                            + arg0.getJSONObject(2).getString("name")); //返回的是JSONArray， 获取JSONArray数据里面的第2个JSONObject对象，然后获取名字为name的数据值
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
            public void onSuccess(JSONObject arg0) {   //返回的是JSONObject，会调用这里
                Log.i("hck", "onSuccess ");
                try {
                    textView.setText("菜谱名字："
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
                    textView.setText("菜谱名字："
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
                    textView.setText("菜谱名字："
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