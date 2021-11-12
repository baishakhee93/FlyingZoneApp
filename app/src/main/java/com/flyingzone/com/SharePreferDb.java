package com.flyingzone.com;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharePreferDb {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    private  static final  String IS_LOGIN="isLogin";
    private  static final  String KEY_FULLNAME="fullName";
    private  static final  String KEY_USERNAME="userName";
    public  static final  String KEY_EMAIL="email";
    private  static final  String KEY_PHONENUMBER="phoneNumber";
    private  static final  String KEY_PASSWORD="password";
   public SharePreferDb(Context _context){
       context =_context;

       sharedPreferences=context.getSharedPreferences("userLogin",Context.MODE_PRIVATE);
       editor=sharedPreferences.edit();
   }



    public void createLogin(String email,String password){
       editor.putBoolean(IS_LOGIN,true);
       editor.putString(KEY_EMAIL,email);
     //  editor.putString(KEY_FULLNAME,name);
       editor.putString(KEY_PASSWORD,password);
       editor.commit();

   }

   public HashMap<String,String> getUserDetails(){
       HashMap<String,String> userData=new HashMap<String,String>();
       userData.put(KEY_EMAIL,sharedPreferences.getString(KEY_EMAIL,null));
       userData.put(KEY_FULLNAME,sharedPreferences.getString(KEY_FULLNAME,null));
       userData.put(KEY_PASSWORD,sharedPreferences.getString(KEY_PASSWORD,null));
       return userData;
   }
   public  boolean checkLogin(){
       if (sharedPreferences.getBoolean(IS_LOGIN,true)){
           return true;
       }else {
           return false;
       }

   }
   public void logOutUser(){
     editor.clear();
     editor.commit();
   }
}
