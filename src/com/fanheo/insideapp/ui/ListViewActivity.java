package com.fanheo.insideapp.ui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.adapter.ListViewAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListViewActivity extends Activity{
	private ListView listView; 
    private ListViewAdapter listViewAdapter; 
    private List<Map<String, Object>> listItems; 
    private Integer[] imgeIDs = {R.drawable.tab_icon4,  
    		R.drawable.tab_icon4,R.drawable.tab_icon4, 
            R.drawable.tab_icon4, R.drawable.tab_icon4, 
            R.drawable.tab_icon4}; 
    private String[] goodsNames = {"����", "����",  
            "��Ʊ", "����", "���", "����CD"}; 
    private String[] goodsDetails = { 
            "���⣺�úóԡ�",  
            "����������ء�",  
            "��Ʊ���������硣",  
            "���ģ����綼�а���", 
            "��꣺��Ӧ���ݡ�", 
            "����CD���������֡�"}; 
    Document doc;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_listview); 
        //loadData();

        listView = (ListView)findViewById(R.id.list_orders);   
        listItems = getListItems(); 
        listViewAdapter = new ListViewAdapter(this, listItems); //���������� 
        listView.setAdapter(listViewAdapter); 
    } 

    /**
     * ��ʼ����Ʒ��Ϣ
     */
    private List<Map<String, Object>> getListItems() { 
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>(); 
        for(int i = 0; i < goodsNames.length; i++) { 
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("image", imgeIDs[i]);               //ͼƬ��Դ 
            map.put("title", "��Ʒ���ƣ�");              //��Ʒ���� 
            map.put("info", goodsNames[i]);     //��Ʒ���� 
            map.put("detail", goodsDetails[i]); //��Ʒ���� 
            listItems.add(map); 
        }    
        return listItems; 
    } 

    /**
     * ����¼�
     * @author Administrator
     *
     */
    class ClickEvent implements OnClickListener{ 

        @Override
        public void onClick(View v) { 
        } 

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
    public void loadData(){
		try {
			doc = Jsoup.parse(new URL("http://www.cnbeta.com"), 5000);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Elements es = doc.getElementsByClass("topic");
		for (Element e : es) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", e.getElementsByTag("a").text());
			map.put("href", "http://www.cnbeta.com"
					+ e.getElementsByTag("a").attr("href"));
			list.add(map);
		}
		
		ListView listView = (ListView) findViewById(R.id.list_orders);
		listView.setAdapter(new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
				new String[] { "title","href" }, new int[] {
				android.R.id.text1,android.R.id.text2
		}));
    	
    }
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	 
	  if (event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
	   new AlertDialog.Builder(this)
	          .setCancelable(false)
	          .setTitle("��ܰ��ʾ")
	          .setMessage("��ȷ��Ҫ�˳���?")
	          .setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int which) { 
	                 finish();
	               
	              }
	          })
	          .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int which) { 
	                
	              }
	          }).show();
	   return true;
	   //��֪������true����false��ʲô����??
	 }
	 
	 return super.dispatchKeyEvent(event);
	 
	}
	

}
