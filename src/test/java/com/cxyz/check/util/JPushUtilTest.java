package com.cxyz.check.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JPushUtilTest {

    @Test
    public void getClient() {
        Map<String,String> map = new HashMap<String, String>();
        map.put("msg","你好啊，世界！");
        JPushUtil.jpushAndroid(map);
    }

    @Test
    public void jpushAndroid() {
    }
}