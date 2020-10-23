package com.supcon.mes.module_sample;

import android.util.Log;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String[] ids={"1","2","3"};
        Flowable.fromArray(ids)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("id="+s);
                    }
                });
    }
}