package com.kodgames.game.start;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.game.service.server.ServerService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by 001 on 2017/4/24.
 * 守卫服务
 */
public class GuardService
{
    private static Logger logger = LoggerFactory.getLogger(GuardService.class);
    private static GuardService instance = new GuardService();

    public static GuardService getInstance()
    {
        return instance;
    }

    /**
     * 开启Guard服务
     * @param port 监听端口号
     */
    public void StartService(int port)
    {
        try
        {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // 添加处理器
            server.createContext("/restartGMT", new HandlerRestartGMT());

            server.start();
        }
        catch (Exception e)
        {
            logger.error("start GuardService failed on port {}", port);
        }
    }

    private static class HandlerRestartGMT implements HttpHandler
    {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException
        {
            logger.debug("HandlerRestartGMT");

            // 简单的ip来源验证
            String remoteIP = httpExchange.getRemoteAddress().getAddress().getHostAddress();
            String remoteName = httpExchange.getRemoteAddress().getHostName();
            logger.debug("remote ip {} name {}", remoteIP, remoteName);
            String localhostName = "127.0.0.1";
            if (!remoteIP.equals(localhostName))
            {
                String response = "illegal request";
                responseString(httpExchange, response);
                return;
            }

            ServerService service = ServiceContainer.getInstance().getPublicService(ServerService.class);
            service.restartGmtServer();

            // response
            String response = "GMT restarted";
            responseString(httpExchange, response);

            logger.warn("GMT restarted by GuardService");
        }

        private void responseString(HttpExchange httpExchange, String response) throws IOException
        {
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
