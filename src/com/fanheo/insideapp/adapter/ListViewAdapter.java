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
		private Context context;                        //���������� 
	    private List<Map<String, Object>> listItems;    //��Ʒ��Ϣ���� 
	    private LayoutInflater listContainer;           //��ͼ���� 
	    private boolean[] hasChecked;                   //��¼��Ʒѡ��״̬ 
	    public final class ListItemView{                //�Զ���ؼ�����   
	            public ImageView image;   
	            public TextView title;   
	            public TextView info; 
	            public CheckBox check; 
	            //public Button detail;        
	     }   

	     
	    public ListViewAdapter(Context context, List<Map<String, Object>> listItems) { 
	        this.context = context;          
	        listContainer = LayoutInflater.from(context);   //������ͼ���������������� 
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
	     * ��¼��ѡ���ĸ���Ʒ
	     * @param checkedID ѡ�е���Ʒ���
	     */
	    private void checkedChange(int checkedID) { 
	        hasChecked[checkedID] = !hasChecked[checkedID]; 
	    } 

	    /**
	     * �ж���Ʒ�Ƿ�ѡ��
	     * @param checkedID ��Ʒ���
	     * @return �����Ƿ�ѡ��״̬
	     */
	    public boolean hasChecked(int checkedID) { 
	        return hasChecked[checkedID]; 
	    } 

	    /**
	     * ��ʾ��Ʒ����
	     * @param clickID
	     */
	    private void showDetailInfo(int clickID) { 
	        new AlertDialog.Builder(context) 
	        .setTitle("��Ʒ���飺" + listItems.get(clickID).get("info")) 
	        .setMessage(listItems.get(clickID).get("detail").toString())               
	        .setPositiveButton("ȷ��", null) 
	        .show(); 
	    } 

	        
	    /**
	     * ListView Item����
	     */
	    public View getView(int position, View convertView, ViewGroup parent) { 
	        // TODO Auto-generated method stub 
	        Log.e("method", "getView"); 
	        final int selectID = position; 
	        //�Զ�����ͼ 
	        ListItemView  listItemView = null; 
	        if (convertView == null) { 
	            listItemView = new ListItemView();  
	            //��ȡlist_item�����ļ�����ͼ 
	            convertView = listContainer.inflate(R.layout.list_item, null); 
	            //��ȡ�ؼ����� 
	            listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem); 
	            listItemView.title = (TextView)convertView.findViewById(R.id.titleItem); 
	            listItemView.info = (TextView)convertView.findViewById(R.id.infoItem); 
	          //listItemView.detail= (Button)convertView.findViewById(R.id.detailItem); 
	            listItemView.check = (CheckBox)convertView.findViewById(R.id.checkItem); 
	            //���ÿؼ�����convertView 
	            convertView.setTag(listItemView); 
	        }else { 
	            listItemView = (ListItemView)convertView.getTag(); 
	        } 
//	      Log.e("image", (String) listItems.get(position).get("title"));  //���� 
//	      Log.e("image", (String) listItems.get(position).get("info")); 

	        //�������ֺ�ͼƬ 
	        listItemView.image.setBackgroundResource((Integer) listItems.get( 
	                position).get("image")); 
	        listItemView.title.setText((String) listItems.get(position) 
	                .get("title")); 
	        listItemView.info.setText((String) listItems.get(position).get("info")); 
	        /*listItemView.detail.setText("��Ʒ����"); 
	        //ע�ᰴť���ʱ�䰮�� 
	        listItemView.detail.setOnClickListener(new View.OnClickListener() { 
	            @Override
	            public void onClick(View v) { 
	                //��ʾ��Ʒ���� 
	                showDetailInfo(selectID); 
	            } 
	        }); */
	        // ע���ѡ��״̬�¼����� 
	        listItemView.check 
	                .setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() { 
	                    @Override
	                    public void onCheckedChanged(CompoundButton buttonView, 
	                            boolean isChecked) { 
	                        //��¼��Ʒѡ��״̬ 
	                        checkedChange(selectID); 
	                    } 
	        }); 

	        return convertView; 
	    } 
	

}
