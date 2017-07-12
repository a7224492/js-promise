package com.mycode.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by jz on 2017/6/30.
 */
public class NettyClient {
    public static void main(String[] args) {
        testLine();
    }

    /**
     * 参考NettyServer testLine()
     */
    public static void testLine() {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new TestLineClientInitializer());

        try {
            b.connect("127.0.0.1", 8888).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TestLineClientInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LoggingHandler());
        p.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4));
        p.addLast(new StringDecoder());
        p.addLast(new LengthFieldPrepender(4));
        p.addLast(new StringEncoder());
        p.addLast(new TestLineClientHandler());
    }
}

class TestLineClientHandler extends SimpleChannelInboundHandler<String> {
    boolean isSend = false;

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Recv msg : "+msg);
        if (!isSend) {
            ctx.channel().writeAndFlush("yse");
            isSend = true;
        }
    }
}