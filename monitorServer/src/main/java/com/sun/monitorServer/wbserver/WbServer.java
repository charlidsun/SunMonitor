package com.sun.monitorServer.wbserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.monitorServer.server.MinitorServerHandler;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket")
@Component
public class WbServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WbServer> webSocketSet = new CopyOnWriteArraySet<WbServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        MinitorServerHandler.getAllMacInfo();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 接收所有机器的基本信息
     * @param msgList
     */
    public static void getMacInfoList(List<String> msgList){
        //组合数据
        Map msgMap = new HashMap();
        //1，接入的客户端数量
        msgMap.put("succMacCount",msgList.size());
        //2，接入客户端信息

    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (WbServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	WbServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	WbServer.onlineCount--;
    }
    
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}