package com.gorod.live;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pxr.tutorial.xmltest.R;

public class MySimpleArrayAdapter extends ArrayAdapter<HashMap<String, String>> {
	private final Activity context;
	private final ArrayList<HashMap<String, String>> names;
	private String val;

	static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	public MySimpleArrayAdapter(Activity context,
			ArrayList<HashMap<String, String>> names) {
		super(context, R.layout.cams, names);
		this.context = context;
		this.names = names;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.cams, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.item_title2);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.fav);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		HashMap<String, String> s = names.get(position);
		holder.text.setText(s.get("title"));
		//if (Storage.checkin(s.get("ex"), Integer.parseInt(s.get("id"))))
			//holder.image.setImageResource(android.R.drawable.btn_star_big_on);

		return rowView;
	}
}
