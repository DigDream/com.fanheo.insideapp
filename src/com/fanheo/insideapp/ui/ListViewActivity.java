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
    private String[] goodsNames = {"蛋糕", "礼物",  
            "邮票", "爱心", "鼠标", "音乐CD"}; 
    private String[] goodsDetails = { 
            "蛋糕：好好吃。",  
            "礼物：礼轻情重。",  
            "邮票：环游世界。",  
            "爱心：世界都有爱。", 
            "鼠标：反应敏捷。", 
            "音乐CD：酷我音乐。"}; 
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_listview); 

        listView = (ListView)findViewById(R.id.list_orders);   
        listItems = getListItems(); 
        listViewAdapter = new ListViewAdapter(this, listItems); //创建适配器 
        listView.setAdapter(listViewAdapter); 
    } 

    /**
     * 初始化商品信息
     */
    private List<Map<String, Object>> getListItems() { 
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>(); 
        for(int i = 0; i < goodsNames.length; i++) { 
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("image", imgeIDs[i]);               //图片资源 
            map.put("title", "物品名称：");              //物品标题 
            map.put("info", goodsNames[i]);     //物品名称 
            map.put("detail", goodsDetails[i]); //物品详情 
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
