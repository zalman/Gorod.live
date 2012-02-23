package com.gorod.live;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pxr.tutorial.xmltest.R;

public class Cams extends ListActivity {

	public static int[] excludes = new int[] { 0, 6 };
	public static final String PREFS_NAME = "FavPref";
	public String jas;
	public static String fav_pre = "fav";
	public SharedPreferences prefs;
	public static String val;

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

		prefs = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		val = prefs.getString(fav_pre, "");
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
				HashMap<String, String> o = (HashMap<String, String>) lv
						.getItemAtPosition(position);

				Intent i = new Intent(Cams.this, CamView.class);
				i.putExtra("id_", o.get("id"));
				i.putExtra("title_", o.get("title"));
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
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

	public class GetCams extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(Cams.this);
		public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

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
			
			MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(Cams.this,
					mylist);
			setListAdapter(adapter);

		}
	}

}
