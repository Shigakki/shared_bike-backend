package com.example.bikes;


import com.alibaba.fastjson.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHeartThread implements Runnable{

    private String host;

    private int port;

    public ClientHeartThread(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host,port);
            System.out.println("心跳socket链接成功");
            //输出流写数据
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //输入流读数据
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            int i =0;
            while (true){

                Thread.sleep(3000);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type","heart");
                jsonObject.put("msg","第"+i+"次心跳");
                System.out.println("发送心跳socket");
                oos.writeObject(jsonObject);
                oos.flush();
                i++;
                String message = ois.readUTF();
                System.out.println("接收到服务端响应"+message);
                if("close".equals(message)){
                    break;
                }
            }
            ois.close();
            oos.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

