package com.gorod.live;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.pxr.tutorial.xmltest.R;

public class Cams extends ListActivity {

	public static int[] excludes = new int[] { 0, 6 };
	public String jas;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final TextView tTemper = (TextView) findViewById(R.id.main_title);
		final ListView lv = getListView();
		
		
		Bundle extras = getIntent().getExtras();
		String id_ = extras.getString("id_");
		String district_ = extras.getString("district_");
		
		jas = extras.getString("JSONArray");
		GetCams(jas);

		String district = getResources().getString(R.string.district);

		if (Arrays.binarySearch(excludes, Integer.parseInt(id_.toString())) > -1)
			tTemper.setText(district_.toString() + ":");
		else
			tTemper.setText(district_.toString() + " " + district.toString());


		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);

				Intent i = new Intent(Cams.this, CamView.class);
				i.putExtra("id_", o.get("id"));
				i.putExtra("title_", o.get("title"));
				startActivity(i);
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_update:
			GetCams(jas);
			return true;

		case R.id.menu_preferences:		
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void GetCams(String str) {
		new GetCams().execute(str);
	}

	private class GetCams extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(Cams.this);
		public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		ListAdapter adapter;

		protected void onPreExecute() {
			Dialog.setMessage(getResources().getString(R.string.loading));
			Dialog.show();
		}

		@Override
		protected Void doInBackground(String... src) {

			try {
				JSONArray cams = new JSONArray(src[0]);

				for (int i = 0; i < cams.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject cl = cams.getJSONObject(i);

					map.put("id", cl.getString("id"));
					map.put("title", cl.getString("title"));
					mylist.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			adapter = new SimpleAdapter(Cams.this, mylist, R.layout.cams,
					new String[] { "title" }, new int[] { R.id.item_title2 });

			setListAdapter(adapter);

		}
	}

	/*
	 * ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String,
	 * String>>(); Bundle extras = getIntent().getExtras(); String id_ =
	 * extras.getString("id_"); JSONObject json =
	 * JSONfunctions.getJSONfromURL("http://contest.podryad.tv/json.php?GetList&id="
	 * +id_);
	 * 
	 * try{
	 * 
	 * 
	 * 
	 * JSONArray cams = json.getJSONArray("CamsList");
	 * 
	 * String district=getResources().getString(R.string.district); String
	 * district_ = extras.getString("district_"); final TextView tTemper =
	 * (TextView) findViewById(R.id.main_title); tTemper.setTextSize(23);
	 * 
	 * if(Arrays.binarySearch(excludes,Integer.parseInt(id_))>-1)
	 * tTemper.setText(district_.toString()); else
	 * tTemper.setText(district_.toString()+" "+district.toString());
	 * 
	 * 
	 * for(int i=0;i<cams.length();i++){ HashMap<String, String> map = new
	 * HashMap<String, String>(); JSONObject cl = cams.getJSONObject(i);
	 * 
	 * map.put("id",cl.getString("id")); map.put("title",cl.getString("title"));
	 * mylist.add(map); }
	 * 
	 * }catch(JSONException e){ Log.e("log_tag",
	 * "Error parsing data "+e.toString()); }
	 * 
	 * ListAdapter adapter = new SimpleAdapter(this, mylist , R.layout.cams, new
	 * String[] {"title"}, new int[] { R.id.item_title2 });
	 * 
	 * setListAdapter(adapter);
	 * 
	 * final ListView lv = getListView(); lv.setTextFilterEnabled(true);
	 * lv.setOnItemClickListener(new OnItemClickListener() { public void
	 * onItemClick(AdapterView<?> parent, View view, int position, long id) {
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * HashMap<String, String> o = (HashMap<String, String>)
	 * lv.getItemAtPosition(position);
	 * 
	 * Intent i = new Intent(Cams.this,CamView.class);
	 * i.putExtra("id_",o.get("id")); i.putExtra("title_",o.get("title"));
	 * startActivity(i); } }); }
	 */

}
