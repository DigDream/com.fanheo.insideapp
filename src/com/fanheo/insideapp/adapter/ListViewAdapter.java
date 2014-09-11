package com.fanheo.insideapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List; 
import java.util.Map; 

import com.fanheo.insideapp.R;

import android.app.AlertDialog; 
import android.content.Context; 
import android.util.Log; 
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.Button; 
import android.widget.CheckBox; 
import android.widget.CompoundButton; 
import android.widget.ImageView; 
import android.widget.ListView; 
import android.widget.TextView; 

public class ListViewAdapter extends BaseAdapter {
		private Context context;                        //运行上下文 
	    private List<Map<String, Object>> listItems;    //商品信息集合 
	    private LayoutInflater listContainer;           //视图容器 
	    private boolean[] hasChecked;                   //记录商品选中状态 
	    public final class ListItemView{                //自定义控件集合   
	            public ImageView image;   
	            public TextView title;   
	            public TextView info; 
	            public CheckBox check; 
	            //public Button detail;        
	     }   

	     
	    public ListViewAdapter(Context context, List<Map<String, Object>> listItems) { 
	        this.context = context;          
	        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文 
	        this.listItems = listItems; 
	        hasChecked = new boolean[getCount()]; 
	    } 

	    public int getCount() { 
	       
	        return listItems.size(); 
	    } 

	    public Object getItem(int arg0) { 
	        
	        return null; 
	    } 

	    public long getItemId(int arg0) { 
	       
	        return 0; 
	    } 

	    /**
	     * 记录勾选了哪个物品
	     * @param checkedID 选中的物品序号
	     */
	    private void checkedChange(int checkedID) { 
	        hasChecked[checkedID] = !hasChecked[checkedID]; 
	    } 

	    /**
	     * 判断物品是否选择
	     * @param checkedID 物品序号
	     * @return 返回是否选中状态
	     */
	    public boolean hasChecked(int checkedID) { 
	        return hasChecked[checkedID]; 
	    } 

	    /**
	     * 显示物品详情
	     * @param clickID
	     */
	    private void showDetailInfo(int clickID) { 
	        new AlertDialog.Builder(context) 
	        .setTitle("物品详情：" + listItems.get(clickID).get("info")) 
	        .setMessage(listItems.get(clickID).get("detail").toString())               
	        .setPositiveButton("确定", null) 
	        .show(); 
	    } 

	        
	    /**
	     * ListView Item设置
	     */
	    public View getView(int position, View convertView, ViewGroup parent) { 
	        // TODO Auto-generated method stub 
	        Log.e("method", "getView"); 
	        final int selectID = position; 
	        //自定义视图 
	        ListItemView  listItemView = null; 
	        if (convertView == null) { 
	            listItemView = new ListItemView();  
	            //获取list_item布局文件的视图 
	            convertView = listContainer.inflate(R.layout.list_item, null); 
	            //获取控件对象 
	            listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem); 
	            listItemView.title = (TextView)convertView.findViewById(R.id.titleItem); 
	            listItemView.info = (TextView)convertView.findViewById(R.id.infoItem); 
	          //listItemView.detail= (Button)convertView.findViewById(R.id.detailItem); 
	            listItemView.check = (CheckBox)convertView.findViewById(R.id.checkItem); 
	            //设置控件集到convertView 
	            convertView.setTag(listItemView); 
	        }else { 
	            listItemView = (ListItemView)convertView.getTag(); 
	        } 
//	      Log.e("image", (String) listItems.get(position).get("title"));  //测试 
//	      Log.e("image", (String) listItems.get(position).get("info")); 

	        //设置文字和图片 
	        listItemView.image.setBackgroundResource((Integer) listItems.get( 
	                position).get("image")); 
	        listItemView.title.setText((String) listItems.get(position) 
	                .get("title")); 
	        listItemView.info.setText((String) listItems.get(position).get("info")); 
	        /*listItemView.detail.setText("商品详情"); 
	        //注册按钮点击时间爱你 
	        listItemView.detail.setOnClickListener(new View.OnClickListener() { 
	            @Override
	            public void onClick(View v) { 
	                //显示物品详情 
	                showDetailInfo(selectID); 
	            } 
	        }); */
	        // 注册多选框状态事件处理 
	        listItemView.check 
	                .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() { 
	                    @Override
	                    public void onCheckedChanged(CompoundButton buttonView, 
	                            boolean isChecked) { 
	                        //记录物品选中状态 
	                        checkedChange(selectID); 
	                    } 
	        }); 

	        return convertView; 
	    } 
	

}
