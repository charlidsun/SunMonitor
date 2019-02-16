package com.sun.monitorClient.protocol;

import com.sun.monitorClient.utils.MachineUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 传输协议类
 */
public class TransProtocol {


    public static Map getMsg(int flag){
        switch (flag) {
            case 1:
                return getBaseInfo();
            default:
                return null;
        }
    }

    private static Map getBaseInfo(){
        Map map = new HashMap();
        map = MachineUtil.getComputerBaseInfo();
        return map;
    }

}
