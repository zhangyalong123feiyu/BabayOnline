package bibi.com.babayonlie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

/**
 * Created by bibinet on 2016/11/28.
 */
public class SharePreferUtil {
    public static SharePreferUtil sharePreferUtil = new SharePreferUtil();
    public static SharedPreferences preferences = null;
    public static final int Model = Context.MODE_PRIVATE;
    public static final String filename = "loginfo";

    public SharePreferUtil() {
    }

    public static SharePreferUtil getSharePreferUtil(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(filename, Model);
        }
        return sharePreferUtil;
    }

    public  String getstring(String key,String defalutvalue){
    return  preferences.getString(key,defalutvalue);
    }

    public  boolean getboolean(String key,boolean defaultvalue){
        return  preferences.getBoolean(key,defaultvalue);
    }
    public    void  putString(String key,String value){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public   void  putboolean(String key,boolean value){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public  void saveuserinfo(String account,String password,boolean isrememberpassword,boolean isautologin){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.putBoolean("isrememberpassword",isrememberpassword);
        editor.putBoolean("isautologin",isautologin);
        editor.commit();

    }
}