package com.sena.dmzjthird.room;

import android.content.Context;

import androidx.room.Room;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/14
 * Time: 20:25
 */
public class RoomHelper {

    private static MyRoomDatabase INSTANCE;

    public static MyRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MyRoomDatabase.class, "download.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
