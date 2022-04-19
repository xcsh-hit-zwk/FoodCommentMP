package com.example.foodcommentmp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.foodcommentmp.common.MD5;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMD5(){
        String s = new String("1233");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + MD5.string2MD5(s));
        System.out.println(s);
        System.out.println("加密的：" + MD5.convertMD5(s));
        System.out.println(s);
        System.out.println("解密的：" + MD5.convertMD5(MD5.convertMD5(s)));
    }
}