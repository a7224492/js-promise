import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jz on 2017/6/27.
 */
public class SocksClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        b.group(group).channel(NioSocketChannel.class).handler(new SocksClientChannelInitializer());

        try {
            b.connect("127.0.0.1", 1080).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SocksClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addFirst(new LoggingHandler());
        channel.pipeline().addLast("SocksMessageEncoder", new SocksMessageEncoder());
        channel.pipeline().addLast("SocksClientHandler", new SocksClientConnectHandler());
    }
}

class SocksConnectEvent {

}

class SocksClientConnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SocksClientConnectHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        List<SocksAuthScheme> authSchemes = new ArrayList<SocksAuthScheme>();
        authSchemes.add(SocksAuthScheme.NO_AUTH);
        SocksInitRequest request = new SocksInitRequest(authSchemes);

        ctx.pipeline().addBefore("SocksClientHandler", "SocksInitResponseDecoder", new SocksInitResponseDecoder());
        ctx.channel().writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof SocksInitResponse) {
            SocksCmdRequest request = new SocksCmdRequest(SocksCmdType.CONNECT, SocksAddressType.IPv4, "127.0.0.1",5000);
            ctx.pipeline().addBefore("SocksClientHandler", "SocksCmdResponseDecoder", new SocksCmdResponseDecoder());
            ctx.channel().writeAndFlush(request);
        } else if (msg instanceof SocksCmdResponse) {
            SocksCmdResponse reponse = (SocksCmdResponse)msg;
            if (reponse.cmdStatus() == SocksCmdStatus.SUCCESS) {
                logger.info("Socksproxy connect success....");
                ctx.pipeline().addLast(new StringEncoder());
                ctx.pipeline().addLast(new StringDecoder());
                ctx.pipeline().addLast(new SocksClientHandler());
                ctx.fireUserEventTriggered(new SocksConnectEvent());
                ctx.pipeline().remove(this);
            } else if (reponse.cmdStatus() == SocksCmdStatus.FAILURE) {
                logger.info("Sockproxy connect fails.....");
            }
        }
    }
}

class SocksClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(SocksClientHandler.class);

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.debug("Recv msg : {}", msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
        if (event instanceof SocksConnectEvent) {
            ctx.channel().writeAndFlush("HelloWorld");
        }
    }
}