package appinventor.ai_ppd1994.buetradioblue.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import appinventor.ai_ppd1994.buetradioblue.R;

public class GridviewAdapter extends BaseAdapter
{
	private ArrayList<String> listMenuItem;
	private ArrayList<Integer> listIcon;
	private Activity activity;
	
	public GridviewAdapter(Activity activity, ArrayList<String> listMenuItem, ArrayList<Integer> listIcon) {
		super();
		this.listMenuItem = listMenuItem;
		this.listIcon = listIcon;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMenuItem.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listMenuItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		public ImageView imgViewFlag;
		public TextView txtViewTitle;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();
		
		if(convertView==null)
		{
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.gridview_row, null);
			
			view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
			view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imageView1);
			
			convertView.setTag(view);
		}
		else
		{
			view = (ViewHolder) convertView.getTag();
		}
		
		view.txtViewTitle.setText(listMenuItem.get(position));
		view.imgViewFlag.setImageResource(listIcon.get(position));
		
		return convertView;
	}

}
