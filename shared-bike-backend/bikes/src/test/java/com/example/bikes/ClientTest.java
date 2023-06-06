package com.example.bikes;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTest {

    private static final ExecutorService  executorService = Executors.newFixedThreadPool(5);

    private static String host = "127.0.0.1";

    private static int port = 5030;

    public static void main(String[] args) {
        executorService.execute(new ClientHeartThread(host,port));
        executorService.execute(new ClientPrintThread(host,port));
//        new Thread(new ClientPrintThread(host,port)).start();
//        new Thread(new ClientHeartThread(host,port)).start();

    }
}

