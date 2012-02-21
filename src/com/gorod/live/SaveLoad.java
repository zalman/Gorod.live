package com.gorod.live;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SaveLoad extends PreferenceActivity{
	SharedPreferences sPref;
	public void saveText(String name,String value) {
		try
		{
	    sPref = getPreferences(MODE_PRIVATE);
	    Editor ed = sPref.edit();
	    ed.putString(name,value);
	    ed.commit();
		}catch(Exception e){
			Log.e("SAVEP",e.getMessage());
		}
	  }
	  
	  public String loadText(String name) {
	    sPref = getPreferences(MODE_PRIVATE);
	    String savedText = sPref.getString("name","");
	    return savedText;
	  }
}
