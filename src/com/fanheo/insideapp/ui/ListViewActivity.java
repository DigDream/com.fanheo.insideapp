package com.fanheo.insideapp.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.adapter.ListViewAdapter;
import com.fanheo.insideapp.data.UserPreferences;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ListViewActivity extends Activity implements
		OnItemClickListener {
	private ListView listView;
	private ListViewAdapter listViewAdapter;
	private List<Map<String, Object>> listItems;
	private Integer[] imgeIDs = { R.drawable.tab_icon4, R.drawable.tab_icon4,
			R.drawable.tab_icon4, R.drawable.tab_icon4, R.drawable.tab_icon4,
			R.drawable.tab_icon4 };
	private String[] goodsNames = { "����", "����", "��Ʊ", "����", "���", "����CD" };
	private String[] goodsDetails = { "���⣺�úóԡ�", "����������ء�", "��Ʊ���������硣",
			"���ģ����綼�а���", "��꣺��Ӧ���ݡ�", "����CD���������֡�" };
	Document doc;
	private PullToRefreshListView mPullRefreshListView;


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			System.out.println("�����ע����ť��");
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
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "ע��");
		return true;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_listview, null);
		setContentView(view);
		
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
		listViewAdapter = new ListViewAdapter(this, listItems); // ����������

		// �������󶨷�������һ
		// ����һ
		// mPullRefreshListView.setAdapter(mAdapter);
		// ������
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView); 
		actualListView.setAdapter(listViewAdapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {
	

			private Cursor cursor1;

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				
				
			   
				Toast.makeText(ListViewActivity.this, "test", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(getApplicationContext(),
						OrderInfoActivity.class);
			    //intent.putExtra("itemInfo", title);
				ListViewActivity.this.startActivity(intent);
			}

		});
		actualListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// When clicked, show a toast with the TextView text
						
						Toast.makeText(ListViewActivity.this, "����С�ĳ�����һ��",
								Toast.LENGTH_LONG).show();
						return false;
					}
				});
		// listView.setAdapter(listViewAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String> {

		// ��̨������
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

		// �����Ƕ�ˢ�µ���Ӧ����������addFirst������addLast()�������¼ӵ����ݼӵ�LISTView��
		// ����AsyncTask��ԭ��onPostExecute���result��ֵ����doInBackground()�ķ���ֵ
		@Override
		protected void onPostExecute(String result) {
			// ��ͷ��������������
			// mListItems.addFirst(result);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgeIDs[1]); // ͼƬ��Դ
			map.put("title", "��Ʒ���ƣ�"); // ��Ʒ����
			map.put("info", goodsNames[1]); // ��Ʒ����
			map.put("detail", goodsDetails[1]); // ��Ʒ����
			listItems.add(map);
			// ֪ͨ�������ݼ��Ѿ��ı䣬�������֪ͨ����ô������ˢ��mListItems�ļ���
			// mAdapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	/**
	 * ��ʼ��������Ϣ
	 */
	private List<Map<String, Object>> getListItems(File file) {
		if (file == null)
			return null;
		XmlPullParser parser = Xml.newPullParser();
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * List<Map<string,string>> list = null; Map<string,string> map = null;
		 */
		try {
			parser.setInput(new FileInputStream(file), "utf-8");

			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("info".equals(parser.getName())) {
						listItems = new ArrayList<Map<String, Object>>();
					} else if ("city".equals(parser.getName())) {
						map = new HashMap<String, Object>();
						map.put("title", parser.getAttributeValue(null, "name"));
						System.out.println(parser.getAttributeValue(null,
								"name"));
					} else if ("weather".equals(parser.getName())) {

						// map.put("detail", parser.nextText()); // ��Ʒ����
						String hash = parser.nextText();
						map.put("info", hash);
						System.out.println(hash);
						map.put("image", imgeIDs[2]);
					}/*
					 * else if("temp".equals(parser.getName())) {
					 * map.put("temp",parser.nextText()); }else
					 * if("wind".equals(parser.getName())) {
					 * map.put("wind",parser.nextText()); }
					 */
					break;
				case XmlPullParser.END_TAG:
					if ("city".equals(parser.getName())) {
						listItems.add(map);
						map = null;
					}
					break;

				default:
					break;
				}
				type = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listItems;
		/*
		 * List<Map<String, Object>> listItems = new ArrayList<Map<String,
		 * Object>>(); Map<String, Object> map = new HashMap<String, Object>();
		 * // ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ InputStream inStream =
		 * OrderParseXmlService
		 * .class.getClassLoader().getResourceAsStream("ver.xml"); // ����XML�ļ���
		 * ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н��� OrderParseXmlService service = new
		 * OrderParseXmlService(); try { map = service.parseXml(inStream); }
		 * catch (Exception e) { e.printStackTrace(); } for (int i = 0; i <
		 * goodsNames.length; i++) { Map<String, Object> map = new
		 * HashMap<String, Object>(); // ��version.xml�ŵ������ϣ�Ȼ���ȡ�ļ���Ϣ InputStream
		 * inStream =
		 * OrderParseXmlService.class.getClassLoader().getResourceAsStream
		 * ("version.xml"); // ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		 * OrderParseXmlService service = new OrderParseXmlService(); try {
		 * mHashMap = service.parseXml(inStream); } catch (Exception e) {
		 * e.printStackTrace(); } map.put("image", imgeIDs[i]); // ͼƬ��Դ
		 * map.put("title", "��Ʒ���ƣ�"); // ��Ʒ���� map.put("info", goodsNames[i]); //
		 * ��Ʒ���� map.put("detail", goodsDetails[i]); // ��Ʒ���� listItems.add(map);
		 * } return listItems;
		 */
	}

	/*
	 * private List<Map<String, Object>> getListItems() { List<Map<String,
	 * Object>> listItems = new ArrayList<Map<String, Object>>(); for (int i =
	 * 0; i < goodsNames.length; i++) { Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("image", imgeIDs[i]); // ͼƬ��Դ
	 * map.put("title", "��Ʒ���ƣ�"); // ��Ʒ���� map.put("info", goodsNames[i]); //
	 * ��Ʒ���� map.put("detail", goodsDetails[i]); // ��Ʒ���� listItems.add(map); }
	 * return listItems; }
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		Log.d("����", "test");
		Toast.makeText(ListViewActivity.this, "test", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(),
				OrderInfoActivity.class);
		ListViewActivity.this.startActivity(intent);
	}

	/**
	 * 
	 * @param urlString
	 * @return �ַ���
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
					.setTitle("��ܰ��ʾ")
					.setMessage("��ȷ��Ҫ�˳���?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			return true;
			// ��֪������true����false��ʲô����??
		}

		return super.dispatchKeyEvent(event);

	}

}
