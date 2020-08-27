package com.supcon.mes.module_sample;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
       String s="{\n" +
               "    \"stdVer1116_result\":null,\n" +
               "    \"specLimit\":[\n" +
               "\n" +
               "    ]\n" +
               "}";
        try {
            JSONObject jsonObject=new JSONObject(s);
            jsonObject.getJSONArray("specLimit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
