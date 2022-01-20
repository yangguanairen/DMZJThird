package com.sena.dmzjthird.utils;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class MyDataStore {

    private static MyDataStore instance = null;

    private final Map<String, RxDataStore<Preferences>> dataStores = new HashMap<>();

    private static final ArrayList<String> names = new ArrayList<>();

    // 文件名
    public static String DATA_STORE_USER = "user";

    // key
    public static String USER_UID = "uid";
    public static String USER_TOKEN = "token";
    public static String USER_NICKNAME = "nickname";
    public static String USER_AVATAR = "avatar";


    static {
        names.add(DATA_STORE_USER);
    }


    public static MyDataStore getInstance(Context context) {
        if (instance == null) {
            synchronized (MyDataStore.class) {
                if (instance == null) {
                    instance = new MyDataStore(context);
                }
            }
        }
        return instance;

    }

    public MyDataStore(Context context) {

        for (String name: names) {
            dataStores.put(
                    name,
                    new RxPreferenceDataStoreBuilder(context, name).build()
            );
        }


    }

    public RxDataStore<Preferences> getDataStore(String name) {
        return dataStores.get(name);
    }

    public <T> T getValue (String dataStoreName, String keyStr, T defValue) {

        Preferences.Key<T> key = convertKey(defValue, keyStr);
        if (key == null) {
            return null;
        }
        Flowable<T> result = getDataStore(dataStoreName).data().map(it -> {
            if (it.get(key) != null) {
                return it.get(key);
            }
            return defValue;
        });

        return result.blockingFirst();

    }

    public <T> void saveValue(String dataStoreName, String keyStr, T value) {

        Preferences.Key<T> key = convertKey(value, keyStr);
        if (key == null) { return; }
        getDataStore(dataStoreName).updateDataAsync(preferences -> {

            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(key, value);

            return Single.just(mutablePreferences);
        });

    }

    private <T> Preferences.Key<T> convertKey(T t, String keyStr) {

        if (t == null) {
            LogUtil.e("数据为空");
            return null;
        }

        if (t instanceof String) {
            return (Preferences.Key<T>) PreferencesKeys.stringKey((keyStr));
        } else if (t instanceof Integer) {
            return (Preferences.Key<T>) PreferencesKeys.intKey((keyStr));
        } else if (t instanceof Float) {
            return (Preferences.Key<T>) PreferencesKeys.floatKey((keyStr));
        } else if (t instanceof Double) {
            return (Preferences.Key<T>) PreferencesKeys.doubleKey((keyStr));
        } else if (t instanceof Boolean) {
            return (Preferences.Key<T>) PreferencesKeys.booleanKey((keyStr));
        } else if (t instanceof Long) {
            return (Preferences.Key<T>) PreferencesKeys.longKey((keyStr));
        } else {
            LogUtil.e("非基本数据类型");
            return null;
        }
    }

}
