package com.example.bikes;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientPrintThread implements Runnable{

    private String host;
    private int port;

    public ClientPrintThread(String host,int port){
        this.host = host;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket(host,port);
            System.out.println("业务socket链接成功");
            //输出流写数据
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //输入流读数据
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type","body");
                jsonObject.put("msg","hello");
                oos.writeObject(jsonObject);
                oos.flush();
                //写的部分
                String message = ois.readUTF();
                System.out.println("接收到服务端响应"+message);
                if("close".equals(message)){
                    break;
                }
            }
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
