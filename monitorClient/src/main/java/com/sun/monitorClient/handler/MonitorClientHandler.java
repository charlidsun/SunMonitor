package com.sun.monitorClient.handler;

import com.sun.monitorClient.protocol.TransProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.Map;

/**
 * 客户端处理逻辑
 */
public class MonitorClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 连接成功
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务器建立连接成功：" + new Date());
        //发送数据
        userEventTriggered(ctx,"eeeee");
    }

    /**
     * 连接失败
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开服务器连接：" + new Date());
    }

    /**
     * 将数据发送给服务器
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void userEventTriggered(ChannelHandlerContext ctx,String msg) throws Exception {
        ctx.channel().writeAndFlush(msg);
    }

    /**
     * 从服务器接受的参数
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("接收到服务器的消息：" + msg.toString());
        String mess = msg.toString();
        Map map = TransProtocol.getMsg(Integer.valueOf(mess));
        //将值返回给服务器
        userEventTriggered(ctx,map.toString());
    }
}
