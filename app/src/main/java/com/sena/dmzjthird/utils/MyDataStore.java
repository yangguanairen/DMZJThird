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
    public static String DATA_STORE_READ_SETTING = "read_setting";
    public static String DATA_STORE_NOVEL_READ_SETTING = "novel_read_setting";

    // key
    public static String USER_UID = "uid";
    public static String USER_TOKEN = "token";
    public static String USER_NICKNAME = "nickname";
    public static String USER_AVATAR = "avatar";


    public static String SETTING_SYSTEM_BRIGHTNESS = "system_brightness";
    public static String SETTING_BRIGHTNESS = "brightness";
    public static String SETTING_VERTICAL_MODE = "vertical_mode";
    public static String SETTING_COMIC_MODE = "comic_mode";
    public static String SETTING_KEEP_LIGHT = "keep_light";
    public static String SETTING_FULL_SCREEN = "full_screen";
    public static String SETTING_SHOW_STATE = "show_state";

    public static String SETTING_NOVEL_TEXT_SIZE = "novel_text_size";
    public static String SETTING_NOVEL_SPACE_LIN = "novel_space_line";
    public static String SETTING_NOVEL_BG = "novel_bg";

    static {
        names.add(DATA_STORE_USER);
        names.add(DATA_STORE_READ_SETTING);
        names.add(DATA_STORE_NOVEL_READ_SETTING);
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

    public void removeEntry(String dataStoreName, String keyStr) {
        getDataStore(dataStoreName).updateDataAsync(preferences -> {
            Preferences.Key<String> key = PreferencesKeys.stringKey(keyStr);
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.remove(key);

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


    public static void clearUserData(Context context) {
        MyDataStore dataStore = MyDataStore.getInstance(context);
        dataStore.removeEntry(DATA_STORE_USER, USER_UID);
        dataStore.removeEntry(DATA_STORE_USER, USER_AVATAR);
        dataStore.removeEntry(DATA_STORE_USER, USER_NICKNAME);
    }

}
