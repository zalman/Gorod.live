package com.gorod.live;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pxr.tutorial.xmltest.R;

public class CamView extends Activity {
	public static final String PREFS_NAME = "FavPref";
	public String gtitle;
	public Integer id;
	public String fav_pre = "fav";
	public int[] fav_list;
	public SharedPreferences prefs;
	public String val;
	public boolean isChangedStat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camview);
		Bundle extras = getIntent().getExtras();
		String id_ = extras.getString("id_");
		id = Integer.parseInt(id_);
		gtitle = extras.getString("title_") + id;
		//
		prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		val = prefs.getString(fav_pre, "");
		//
		GetImage("http://contest.podryad.tv/json.php?GetImage&id="
				+ id.toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		if (Storage.checkin(val, id))
			menu.add(0, 13337, 2, getResources().getString(R.string.infav))
					.setIcon(android.R.drawable.btn_star_big_on);
		else
			menu.add(0, 13337, 2, getResources().getString(R.string.fav))
					.setIcon(android.R.drawable.btn_star_big_off);
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		if (Storage.checkin(val, id))
			menu.findItem(13337).setIcon(android.R.drawable.btn_star_big_on)
					.setTitle(getResources().getString(R.string.infav));
		else
			menu.findItem(13337).setIcon(android.R.drawable.btn_star_big_off)
					.setTitle(getResources().getString(R.string.fav));
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_update:
			GetImage("http://contest.podryad.tv/json.php?GetImage&id="
					+ id.toString());
			return true;
		case 13337:
			if (Storage.checkin(val, id)) {
				val = Storage.list_remove(val, id);
				Toast.makeText(this,
						getResources().getString(R.string.deleted),
						Toast.LENGTH_SHORT).show();
				isChangedStat = true;
			} else {
				val = Storage.list_add(val, id);
				Toast.makeText(this, getResources().getString(R.string.saved),
						Toast.LENGTH_SHORT).show();
				isChangedStat = false;
			}
			prefs.edit().putString(fav_pre, val).commit();

			return true;

		/*case R.id.menu_preferences:
			if (val != "")
				Toast.makeText(this, val, Toast.LENGTH_SHORT).show();
			return true;*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void GetImage(String url) {
		if (Utils.isNetworkAvailable(this.getApplicationContext()))
			new GetImage().execute(url);
		else
			Toast.makeText(this,
					getResources().getString(R.string.no_internet),
					Toast.LENGTH_SHORT).show();
	}

	private class GetImage extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(CamView.this);
		final ImageView iv = (ImageView) findViewById(R.id.image);
		final TextView title = (TextView) findViewById(R.id.text);
		final TextView notice = (TextView) findViewById(R.id.notice);
		Bitmap bmp;

		protected void onPreExecute() {
			Dialog.setMessage(getResources().getString(R.string.loading));
			Dialog.show();
		}

		protected Void doInBackground(String... urls) {
			try {

				JSONObject json = JSONfunctions.getJSONfromURL(urls[0]);

				String image_ = json.getString("Image");

				byte[] decodedString = Base64Coder.decode(image_);
				if (bmp != null) {
					bmp.recycle();
					bmp = null;
				}
				bmp = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length);

			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing json " + e.toString());
				notice.setText("Error parsing json " + e.toString());

			} catch (Exception e) {
				Log.e("log_tag", "Error " + e.toString());
				notice.setText("Error " + e.toString());
			}
			return null;
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			iv.setImageBitmap(bmp);
			title.setText(gtitle.toString());
		}
	}

}
