package com.example.bikes;

import com.alibaba.fastjson.JSONObject;
import com.common.entity.Bike;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerReceiveThread implements Runnable {
    private String csvFilePath = "common/bike_positions.csv"; // 指定CSV文件路径
    private Socket socket;

    private static Logger log = LoggerFactory.getLogger(ServerReceiveThread.class);

    public ServerReceiveThread(Socket socket) {
        this.socket = socket;
    }

    public void appendToCSV(Bike bike, String time) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.csvFilePath, true))) {
            // 创建一个CSVWriter对象并传入FileWriter，以便将数据追加到CSV文件末尾
            int id = bike.getId();
            String x = bike.getX();
            String y = bike.getY();
            String status = bike.getStatus();
            String owner = bike.getOwner();
            // 追加数据行
            String[] data = {String.valueOf(id), x, y, status, owner, time};
            writer.writeNext(data);

            // 注意：每个数据行都需要使用字符串数组来表示
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //输入流接收数据
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //输出流发送数据
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                JSONObject jsonObject = (JSONObject) ois.readObject();
                System.out.println(jsonObject.toJSONString());
                Bike bike = (Bike) jsonObject.getObject("bike",Bike.class);
                String time = jsonObject.getString("time");
                String message = jsonObject.getString("msg");;
                if ("close".equals(message)) {
                    oos.writeUTF("close");
                    oos.flush();
                    break;
                } else {
                    oos.writeUTF("接收数据成功" + message);
                    this.appendToCSV(bike,time);
                    oos.flush();
                }
            }
            log.info("服务端关闭客户端[{}]", socket.getRemoteSocketAddress());
            oos.close();
            ois.close();
            socket.close();
        } catch (Exception e) {
            log.info("接收数据异常socket关闭");
            e.printStackTrace();
        } finally {
            log.info("数据异常数据要怎么保留");
        }
    }
}

