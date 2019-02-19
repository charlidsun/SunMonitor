package com.sun.monitorServer.test;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Te {

    public static void main(String[] args) {
        String m = "{'ip':'172.172.100.123'}";
        JSONObject o = JSONObject.parseObject(m);
        System.out.println(o.get("ip"));
    }
}
