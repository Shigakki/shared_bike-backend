package com.example.bikes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ServerSocketConfig {

    private static Logger log = LoggerFactory.getLogger(ServerSocketConfig.class);

    public static ServerSocket serverSocket = null;

    private static final ThreadPoolExecutor threadpool = new ThreadPoolExecutor(15, 15,
            10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Bean
    public void socketCreate() {
        try {
            serverSocket = new ServerSocket(5030);
            log.info("socket服务端开启");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("接收到客户端socket" + socket.getRemoteSocketAddress());
                threadpool.execute(new ServerReceiveThread(socket));
            }
        } catch (IOException e) {
            log.info("socket服务启动异常");
            e.printStackTrace();
        }
    }
}

