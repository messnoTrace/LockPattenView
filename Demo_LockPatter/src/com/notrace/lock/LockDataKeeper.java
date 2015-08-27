package com.notrace.lock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LockDataKeeper
{
	private static final String PREFERENCES_NAME = "_VER_DATA_KEEPER";
	
	public static String getUserName(Context context) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getString("userName", null);
	}

	public static void setUserName(Context context, String userName) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("userName", userName);
		editor.commit();
	}
	
	public static String getLockPass(Context context) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getString("lockPass", null);
	}
	
	public static void setLockPass(Context context, String userName) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("lockPass", userName);
		editor.commit();
	}
	
	public static boolean getIsLogin(Context context) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getBoolean("isLogin", false);
	}
	
	public static void setIsLogin(Context context, boolean isLogin) {
		SharedPreferences pref = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}
}