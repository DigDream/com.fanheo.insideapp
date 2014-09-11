package com.fanheo.insideapp.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fanheo.insideapp.R;
import com.fanheo.insideapp.adapter.ListViewAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

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
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_listview); 

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

    class ClickEvent implements OnClickListener{ 

        @Override
        public void onClick(View v) { 
        } 

    }  

}
