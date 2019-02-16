package com.sun.monitorServer.server;

import com.sun.monitorServer.wbserver.WbServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MinitorServerHandler extends SimpleChannelInboundHandler<String> {

    //线程安全，保存客户端信息
    private static Map<Channel, Object> clientMap = new ConcurrentHashMap<>();

    private static List<String> allMsgList = new ArrayList<>();

    /**
     * 连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("有新的客户端添加进来了" + channel);
        //获取成功后，向客户端请求信息
        System.out.println("请求客户端基本数据");
        sendMsg(channel,"1");
        clientMap.put(channel,"");

    }

    /**
     * 断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("有客户端离开" + ctx.channel());
        clientMap.remove(ctx.channel());
    }


    /**
     * 读客户端发送过来的消息
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println(msg);
        allMsgList.add(msg);
        if (allMsgList.size() == clientMap.size()){
            WbServer.sendInfo(allMsgList.toString());
            allMsgList.clear();
        }
    }

    /**
     * 获取所有机器的信息
     * @return
     */
    public static void getAllMacInfo(){
        System.out.printf("////////////////");
        allMsgList.clear();
        for (Channel channel : clientMap.keySet()){
            System.out.printf("开始请求。。。");
            sendMsg(channel,"1");
        }
    }


    public static void sendMsg(Channel channel,String msg){
        System.out.printf("发送数据");
        channel.writeAndFlush(msg);
    }


    public void addChannel(Channel channel,String ip){
        clientMap.put(channel,ip);
    }

    public void removeChannel(Channel channel){
        clientMap.remove(channel);
    }

}
