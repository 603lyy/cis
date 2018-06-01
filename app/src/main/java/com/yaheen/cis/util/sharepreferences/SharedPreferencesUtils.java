package com.yaheen.cis.util.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

/**
 * Created by linjingsheng on 17/3/2.
 */

public class SharedPreferencesUtils {

    private static SharedPreferences sp=null;
    private static SharedPreferencesUtils instance;
    private String defName="default";
    private SharedPreferencesUtils(){

    }


    private SharedPreferencesUtils(Context context){
        sp= context.getSharedPreferences(defName, Context.MODE_PRIVATE);
    }
    private SharedPreferencesUtils(Context context, String shareName){
        sp= context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
    }

    public  static SharedPreferencesUtils createSharedPreferences(String Name, Context context){
        if (instance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtils(context, Name);
                }
            }
        }
        return instance;
    }



    public void put(String key, boolean value) {
        SharedPreferences.Editor edit = sp.edit();
        if (edit != null) {
            edit.putBoolean(key, value);
            edit.commit();
        }
    }

    public void put(String key, String value) {
        SharedPreferences.Editor edit = sp.edit();
        if (edit != null) {
            edit.putString(key, value);
            edit.commit();
        }
    }

    public void put(String key, int value) {
        SharedPreferences.Editor  edit = sp.edit();
        if (edit != null) {
            edit.putInt(key, value);
            edit.commit();
        }
    }

    public void put(String key, float value) {
        SharedPreferences.Editor  edit = sp.edit();
        if (edit != null) {
            edit.putFloat(key, value);
            edit.commit();
        }
    }

    public void put(String key, long value) {
        SharedPreferences.Editor  edit = sp.edit();
        if (edit != null) {
            edit.putLong(key, value);
            edit.commit();
        }
    }

    public void put(String key, Set<String> value) {
        SharedPreferences.Editor  edit = sp.edit();
        if (edit != null) {
            edit.putStringSet(key, value);
            edit.commit();
        }
    }




    public String get(String key) {
        return sp.getString(key, "");
    }

    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public int get(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public Set<String> get(String key, Set<String> defValue) {
        return sp.getStringSet(key, defValue);
    }


    public void put(String key, Object object) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            // 然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor edit = sp.edit();
            if (edit != null) {
                edit.putString(key, objectVal);
                edit.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            // 一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



}
