package com.gorod.live;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceActivity;

public class SaveLoad extends PreferenceActivity{
	public static final String PREFS_NAME = "FavPref";
	SharedPreferences sPref;
	public void saveText(String name,String value) {
		try
		{
		SharedPreferences sPref = getSharedPreferences(PREFS_NAME,0);
	    Editor ed = sPref.edit();
	    ed.putString("n","222");
	    ed.commit();
		}catch(Exception e){
			System.out.println("error while saving "  + e.toString());
		}
	  }
	  
	  public String loadText(String name) {
		String savedText = null;
		try{
	    sPref = getSharedPreferences(PREFS_NAME,0);
	    savedText = sPref.getString("name","");
		}catch(Exception e){
			System.out.println("error while loading " + e.toString());
		}
		return savedText;
	  }
}
