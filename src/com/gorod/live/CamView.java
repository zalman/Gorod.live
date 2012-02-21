package com.gorod.live;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pxr.tutorial.xmltest.R;

public class CamView extends Activity {
	public String gtitle;
	public int id_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camview);
		Bundle extras = getIntent().getExtras();
		id_ = extras.getInt("id_");
		gtitle = extras.getString("title_");
		GetImage("http://contest.podryad.tv/json.php?GetImage&id=" + id_);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menu.add(0, 13337, 2, getResources().getString(R.string.fav)).setIcon(android.R.drawable.btn_star_big_off);
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_update:
			GetImage("http://contest.podryad.tv/json.php?GetImage&id=" + id_);
			return true;
		case 13337:
			new SaveLoad().saveText("favs","1");
			return true;

		case R.id.menu_preferences:
			//String st=new SaveLoad().loadText("favs");
			//System.out.println(st.toString());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void GetImage(String url) {
		new GetImage().execute(url);
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
