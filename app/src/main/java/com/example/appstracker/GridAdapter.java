package com.example.appstracker;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstracker.model.AppInfo;

public class GridAdapter extends BaseAdapter {

	Context context;
	List<AppInfo> apps,selectedApps;
	ImageView appimage;
	TextView appname;

	public GridAdapter(List<AppInfo> apps, List<AppInfo> selectedApps, Context context) {
		this.apps = apps;
		this.context = context;
		this.selectedApps=selectedApps;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo AppInfo=new AppInfo();
		View v = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (v == null) {
			v=inflater.inflate(R.layout.custom_list_item, null, false);

		}
		appimage=(ImageView)v.findViewById(R.id.appimages);
		appname=(TextView)v.findViewById(R.id.apptext);
		appimage.setBackgroundDrawable(apps.get(position).getIcon());
		appname.setText(apps.get(position).getAppname());
		
		TextView totalTime=(TextView)v.findViewById(R.id.totalTimeID);
		TextView remianTime=(TextView)v.findViewById(R.id.remainTimeID);
		
		
		
		for(int i=0;i<selectedApps.size();i++)
		{
			if(selectedApps.get(i).getAppname().equals(apps.get(position).getAppname()))
					{
				
			//	Toast.makeText(context, apps.get(position).getAppname(), 1000).show();
				
				v.setBackgroundResource(R.drawable.applicationlist_item_selected);
				
				totalTime.setText("Total Time: "
						+ Long.toString(selectedApps.get(i).getTotalTime()/60) + " Min");
				remianTime.setText("Remaining Time: "
						+ Long.toString(selectedApps.get(i).getTotalTime()/60) + " Min");
					}
		}
	
		/*if(selectedApps.contains(apps.get(position)))
		{
			v.setBackgroundResource(R.drawable.applicationlist_item_selected);
			
			totalTime.setText("Total Time: "
					+ Long.toString(selectedApps.get(position).getTotalTime()/60) + " Min");
			remianTime.setText("Remaining Time: "
					+ Long.toString(selectedApps.get(position).getRemainTime()/60) + " Min");
		}*/
		
		
		/*final LinearLayout gridLL=(LinearLayout)v.findViewById(R.id.gridRLID);
		
		gridLL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				gridLL.setBackgroundResource(R.drawable.applicationlist_item_selected);
				
			}
		});*/
		
		return v;
	}

}
