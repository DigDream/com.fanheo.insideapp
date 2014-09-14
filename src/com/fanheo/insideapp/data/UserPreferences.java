package com.fanheo.insideapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

@SuppressLint("NewApi")
public class UserPreferences {
	private final String AUTO_LOGIN = "auto_login";
	private SharedPreferences userPreferences;
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String USERID = "";
	
	/**
	 * ≥ı ºªØSharePreferences
	 */
	public void init(Context ctx){
		if(null == userPreferences){
			userPreferences = ctx.getSharedPreferences("fanheo", Context.MODE_PRIVATE);
			
		}
	}
	public void saveName(String name){
		Editor editor = userPreferences.edit();
		String fName = new String (Base64.encode(name.getBytes(),Base64.DEFAULT));
		editor.putString(USERNAME, fName);
		editor.commit();
	}
	public String getName(){
		String fName = userPreferences.getString(USERNAME, "");
		return new String(Base64.decode(fName, Base64.DEFAULT));
	}
	public void savePWD(String password){
		Editor editor = userPreferences.edit();
		String fpwd = new String (Base64.encode(password.getBytes(),Base64.DEFAULT));
		editor.putString(PASSWORD, fpwd);
		editor.commit();
	}
	public String getPWD(){
		String fpwd = userPreferences.getString(PASSWORD, "");
		return new String(Base64.decode(fpwd, Base64.DEFAULT));
	}
	public void saveUID(String uid){
		Editor editor = userPreferences.edit();
		editor.putString(USERID, uid);
		editor.commit();
	}
	public String getUID(){
		String uid = userPreferences.getString(USERID, "no");
		return uid;
	}
	public void setAutoLogin(boolean autoLogin){
		Editor editor = userPreferences.edit();
		editor.putBoolean(AUTO_LOGIN,autoLogin);
		editor.commit();
	}
	public boolean getAutoLogin(){
		return userPreferences.getBoolean(AUTO_LOGIN, false);
		
	}

}
