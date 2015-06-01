package movies.search.app.globals;

import android.content.Context;
import android.content.SharedPreferences;

public class Globals {

	public static void setConfig(Context c, String data){
		SharedPreferences config = c.getSharedPreferences("config", 0);
		SharedPreferences.Editor prefs_editor = config.edit();
		prefs_editor.putString("config", data);
		prefs_editor.commit();		
	}

	public static String getConfig(Context c){
		SharedPreferences config = c.getSharedPreferences("config", 0);
		return config.getString("config", "");
	}
}
