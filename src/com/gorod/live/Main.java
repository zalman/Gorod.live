package com.gorod.live;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class Main extends ListActivity {
	public JSONArray CamsList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		GetData("http://contest.podryad.tv/json.php?GetList");
		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);

				Intent i = new Intent(Main.this, Cams.class);
				i.putExtra("id_", o.get("id"));
				i.putExtra("district_", o.get("title"));
				i.putExtra("JSONArray", CamsList.toString());
				startActivity(i);
			}

		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_update:
			GetData("http://contest.podryad.tv/json.php?GetList");
			return true;

		case R.id.menu_preferences:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void GetData(String url) {
		new GetData().execute(url);
	}

	private class GetData extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(Main.this);
		public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		ListAdapter adapter;
		final TextView tTemper = (TextView) findViewById(R.id.main_title);
		final TextView notice = (TextView) findViewById(R.id.notice);

		protected void onPreExecute() {
			Dialog.setMessage(getResources().getString(R.string.loading));
			Dialog.show();
		}

		@Override
		protected Void doInBackground(String... urls) {

			try {
				JSONObject json = JSONfunctions.getJSONfromURL(urls[0]);

				JSONArray districts = json.getJSONArray("DistrictsList");
				CamsList = json.getJSONArray("CamsList");
				String count = getResources().getString(R.string.count) + " ";
				for (int i = 0; i < districts.length(); i++) {
					JSONObject dl = districts.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", dl.getString("id"));
					map.put("title", dl.getString("title"));
					map.put("count", count + " " + dl.getString("count"));
					mylist.add(map);
				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				notice.append("Error parsing data " + e.getMessage());
			} catch (Exception e) {
				Log.e("log_tag", "Error " + e.toString());
				notice.append("Error " + e.getMessage());

			}
			return null;

		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			adapter = new SimpleAdapter(Main.this, mylist, R.layout.listview,
					new String[] { "title", "count" }, new int[] {
							R.id.item_title, R.id.item_subtitle });

			setListAdapter(adapter);

			String main_title = getResources().getString(R.string.main_title);

			tTemper.setText(main_title.toString());

		}
	}

}