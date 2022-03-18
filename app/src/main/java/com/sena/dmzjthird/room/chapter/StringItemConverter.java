package com.sena.dmzjthird.room.chapter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * FileName: StringItemConverter
 * Author: JiaoCan
 * Date: 2022/3/18 10:47
 */

public class StringItemConverter {
    @TypeConverter
    public String objectToString(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public List<String> stringToObject(String json) {
        Type listType = new TypeToken<List<String>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }
}