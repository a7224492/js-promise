package com.mycode.test;

import com.sun.security.ntlm.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by jz on 2017/6/30.
 */
public class NettyServer {
    public static void main(String[] args) {
        testLine();
    }

    /**
     * 测试channel read的时候，是否会以换行符来分割
     * 比如：
     *  一个包的数据是：abcd\nefg,是否会读两次，第一次的数据是abcd\n，第二次的数据是efg
     */
    public static void testLine() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup io = new NioEventLoopGroup();

        final boolean SSL = System.getProperty("ssl") != null;
        SslContext sslCtx = null;
        try {
            // Configure SSL.
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, io)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler())
                .childHandler(new TestLineServerInitializer(sslCtx));

        try {
            b.bind(8888).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TestLineServerInitializer extends ChannelInitializer<SocketChannel> {
    private SslContext sslCtx;

    public TestLineServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new LengthFieldPrepender(4));
        p.addLast(new StringEncoder());
        p.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4));
        p.addLast(new StringDecoder());
        p.addLast(new ChunkedWriteHandler());
        p.addLast(new TestLineServerHandler());
    }
}

class TestLineServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().write("connection");
        ctx.channel().writeAndFlush("active");
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        ctx.channel().write("abcd");
        ctx.channel().writeAndFlush("efg");
    }
}