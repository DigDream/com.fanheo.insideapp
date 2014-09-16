package com.fanheo.insideapp.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.adapter.ListViewAdapter;
import com.fanheo.insideapp.data.UserPreferences;
import com.fanheo.insideapp.util.OrderParseXmlService;
import com.fanheo.insideapp.util.ParseXmlService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListViewActivity extends Activity {
	private ListView listView;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> listItems;
	private Integer[] imgeIDs = { R.drawable.tab_icon4, R.drawable.tab_icon4,
			R.drawable.tab_icon4, R.drawable.tab_icon4, R.drawable.tab_icon4,
			R.drawable.tab_icon4 };
	private String[] goodsNames = { "蛋糕", "礼物", "邮票", "爱心", "鼠标", "音乐CD" };
	private String[] goodsDetails = { "蛋糕：好好吃。", "礼物：礼轻情重。", "邮票：环游世界。",
			"爱心：世界都有爱。", "鼠标：反应敏捷。", "音乐CD：酷我音乐。" };
	Document doc;
	private PullToRefreshListView mPullRefreshListView;
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 		case Menu.FIRST + 1:  
 			System.out.println("点击了注销按钮！");
			//Toast.makeText(ListViewActivity.this, "存在网络", Toast.LENGTH_LONG).show();
	 		UserPreferences preferences = new UserPreferences();
			preferences.init(ListViewActivity.this);
			preferences.setAutoLogin(true);
			Intent intent = new Intent();
			intent.setClass(ListViewActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
 		}
 		return super.onOptionsItemSelected(item);
 	}
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, Menu.FIRST + 1, 5, "注销");
		return true;
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		// loadData();
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// Do work to refresh the list here.
						new GetDataTask().execute();
					}
				});

		// mListItems = new LinkedList<String>();
		// mListItems.addAll(Arrays.asList(mStrings));

		// mAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, mListItems);

		// listView = (ListView)findViewById(R.id.pull_refresh_list);
		File file = new File("/sdcard/ver.xml");	
		listItems = getListItems(file);
		listViewAdapter = new ListViewAdapter(this, listItems); // 创建适配器
	        
		// 这两个绑定方法用其一
		// 方法一
		// mPullRefreshListView.setAdapter(mAdapter);
		// 方法二
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(listViewAdapter);
		// listView.setAdapter(listViewAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		// 后台处理部分
		@Override
		protected String doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			String str = "Added after refresh...I add";
			return str;
		}

		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String result) {
			// 在头部增加新添内容
			// mListItems.addFirst(result);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgeIDs[1]); // 图片资源
			map.put("title", "物品名称："); // 物品标题
			map.put("info", goodsNames[1]); // 物品名称
			map.put("detail", goodsDetails[1]); // 物品详情
			listItems.add(map);
			// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
			// mAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * 初始化商品信息
	 */
	private List<Map<String, Object>> getListItems(File file) {
		if(file == null)
            return null;
        XmlPullParser parser = Xml.newPullParser();
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
       /* List<Map<string,string>> list = null;
        Map<string,string> map = null;*/
        try
        {
            parser.setInput(new FileInputStream(file),"utf-8");
             
            int type = parser.getEventType();
            while(type != XmlPullParser.END_DOCUMENT)
            {
                switch (type)
                {
                case XmlPullParser.START_TAG:
                    if("info".equals(parser.getName()))
                    {
                    	listItems = new ArrayList<Map<String,Object>>();
                    }else if("city".equals(parser.getName()))
                    {
                        map = new HashMap<String, Object>();
                        map.put("title",parser.getAttributeValue(null,"name"));
                        System.out.println(parser.getAttributeValue(null,"name"));
                    }else if("weather".equals(parser.getName()))
                    {
                       
                       // map.put("detail", parser.nextText()); // 物品详情
                        String hash = parser.nextText();  
                        map.put("info",hash);
                        System.out.println(hash);
                        map.put("image", imgeIDs[2]);
                    }/*else if("temp".equals(parser.getName()))
                    {
                        map.put("temp",parser.nextText());
                    }else if("wind".equals(parser.getName()))
                    {
                        map.put("wind",parser.nextText());
                    }*/
                    break;
                case XmlPullParser.END_TAG:
                    if("city".equals(parser.getName()))
                    {
                    	listItems.add(map);
                        map = null;
                    }
                    break;
                     
                default:
                    break;
                }
                type = parser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return listItems;
		/*List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 把version.xml放到网络上，然后获取文件信息
		InputStream inStream = OrderParseXmlService.class.getClassLoader().getResourceAsStream("ver.xml");
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		OrderParseXmlService service = new OrderParseXmlService();
		try {
			map = service.parseXml(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < goodsNames.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 把version.xml放到网络上，然后获取文件信息
			InputStream inStream = OrderParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
			// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
			OrderParseXmlService service = new OrderParseXmlService();
			try
			{
				mHashMap = service.parseXml(inStream);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			map.put("image", imgeIDs[i]); // 图片资源
			map.put("title", "物品名称："); // 物品标题
			map.put("info", goodsNames[i]); // 物品名称
			map.put("detail", goodsDetails[i]); // 物品详情
			listItems.add(map);
		}
		return listItems;*/
	}
	/*private List<Map<String, Object>> getListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < goodsNames.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgeIDs[i]); // 图片资源
			map.put("title", "物品名称："); // 物品标题
			map.put("info", goodsNames[i]); // 物品名称
			map.put("detail", goodsDetails[i]); // 物品详情
			listItems.add(map);
		}
		return listItems;
	}*/

	/**
	 * 点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View v) {
		}

	}

	/**
	 * 
	 * @param urlString
	 * @return 字符串
	 */
	public String getHtmlString(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection ucon = url.openConnection();
			InputStream instr = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(instr);
			ByteArrayBuffer baf = new ByteArrayBuffer(500);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			return EncodingUtils.getString(baf.toByteArray(), "gbk");
		} catch (Exception e) {
			return "";
		}
	}

	/**
     * 
     */
	/*
	 * public void loadData(){ try { doc = Jsoup.parse(new
	 * URL("http://www.cnbeta.com"), 5000); } catch (MalformedURLException e1) {
	 * e1.printStackTrace(); } catch (IOException e1) { e1.printStackTrace(); }
	 * 
	 * List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	 * Elements es = doc.getElementsByClass("topic"); for (Element e : es) {
	 * Map<String, String> map = new HashMap<String, String>(); map.put("title",
	 * e.getElementsByTag("a").text()); map.put("href", "http://www.cnbeta.com"
	 * + e.getElementsByTag("a").attr("href")); list.add(map); }
	 * 
	 * //ListView listView = (ListView) findViewById(R.id.list_orders);
	 * listView.setAdapter(new SimpleAdapter(this, list,
	 * android.R.layout.simple_list_item_2, new String[] { "title","href" }, new
	 * int[] { android.R.id.text1,android.R.id.text2 }));
	 * 
	 * }
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setCancelable(false)
					.setTitle("温馨提示")
					.setMessage("您确定要退出吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			return true;
			// 不知道返回true或是false有什么区别??
		}

		return super.dispatchKeyEvent(event);

	}

}
