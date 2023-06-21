package com.example.bikes.controller;

import com.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/ml")
public class MLController {
    public static Socket socket=null;

    String csvFilePath = "shared-bike-backend/common/bike_positions.csv"; // ָ��CSV�ļ�·��
    String content = csvFilePath;

    public static List<String> getKeyWord(String content){
        Socket socket = null;
        String Code_Adress = "127.0.0.1";
        List<String>result=new LinkedList<>();
        try {
            socket = new Socket("127.0.0.1", 8081);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            outputStream.write(content.getBytes());

            int len = inputStream.read(bytes);

            System.out.println(len);
            String str = new String(bytes,0,len);
            String[] list=str.split(",");
            Collections.addAll(result, list);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping
    public Result<?> getMLResult(){
        List<String> theResult = getKeyWord(csvFilePath);
        System.out.println("����ѧϰ���ص�����:" + theResult.toString());
        return Result.success(theResult);
    }
}
