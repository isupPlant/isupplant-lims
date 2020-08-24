package com.supcon.mes.module_lims;

import com.supcon.mes.module_lims.utils.Util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        boolean flag=Util.parseStrInRange("10","(24,50)");
    }
}