import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jz on 2017/6/30.
 */
public class FileClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new FileClientHandlerInitializer());

        try {
            b.connect("127.0.0.1", 8023).sync();
            System.out.println("connect finish ....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class FileClientHandlerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler());
//        ch.pipeline().addLast(new LengthFieldPrepender(4));
//        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
//        ch.pipeline().addLast(new LineBasedFrameDecoder(8192));
        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        ch.pipeline().addLast(new FileClientHandler());
    }
}

class FileClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(FileClientHandler.class);
    boolean isSendFileRequest = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("Recv msg : {}", msg);
        if (!isSendFileRequest) {
            ctx.channel().writeAndFlush("d:/helloworld.txt"+System.getProperty("line.separator"));
            isSendFileRequest = true;
        }
    }
}
