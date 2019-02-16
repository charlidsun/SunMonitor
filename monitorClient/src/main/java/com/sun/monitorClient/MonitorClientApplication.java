package com.sun.monitorClient;


import com.sun.monitorClient.handler.MonitorClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MonitorClientApplication {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //检测连接有效性（心跳）,此处功能：5秒内write()未被调用则触发一次useEventTrigger()方法
                            ch.pipeline().addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast(new MonitorClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 9091).sync();
            future.channel().closeFuture().sync();
        }catch (Exception e){
            System.err.println("连接失败，请确认服务器是否开启，或者配置是否正确");
        }
        finally {
            group.shutdownGracefully();
        }

    }
}
