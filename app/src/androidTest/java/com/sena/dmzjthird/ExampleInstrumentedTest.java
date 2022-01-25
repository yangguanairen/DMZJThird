package com.sena.dmzjthird;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Pattern p1 = Pattern.compile("[a-zA-Z0-9]{6,16}");
    private Pattern p2 = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]{4,12}");

    @Test
    public void useAppContext() {

        System.out.println(p1.matcher("123abc").find());
        System.out.println(p1.matcher("123 abc").matches());
        System.out.println(p1.matcher("123所发生的").matches());
        System.out.println(p2.matcher("123abc士大夫").matches());
        System.out.println(p2.matcher("123 abc 是否是").matches());
        System.out.println(p2.matcher("123所发生的；‘【、").matches());



    }
}