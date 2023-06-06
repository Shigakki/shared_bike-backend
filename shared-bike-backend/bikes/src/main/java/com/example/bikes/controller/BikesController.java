package com.example.bikes.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.Result;
import com.common.entity.Bike;
import com.example.bikes.common.UpdateBikeRequest;
import com.example.bikes.mapper.BikesMapper;
import com.opencsv.CSVWriter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
@RequestMapping("/bikes")
public class BikesController {
    @Resource
    BikesMapper bikesMapper;

    /**
     * @param bike:
     * @return Result<?>
     * @author Shichao
     * @description 添加单车
     */
    @PostMapping
    public Result<?> addBike(@RequestBody Bike bike){
        System.out.println("添加共享单车");

        bikesMapper.insert(bike);
        return Result.success();
    }

    /**
     * @param bike:
     * @return Result<?>
     * @author Shichao
     * @description 更新单车的信息
     */
    String csvFilePath = "shared-bike-backend/common/bike_positions.csv"; // 指定CSV文件路径
    @PutMapping
    public Result<?> updateBike(@RequestBody UpdateBikeRequest updateBikeRequest){
        System.out.println("更新单车信息");
        Bike bike = updateBikeRequest.getBike();
        String time = updateBikeRequest.getTime();
        bikesMapper.updateById(bike);
        // 写到csv中
        int id = bike.getId();
        String x = bike.getX();
        String y = bike.getY();
        String status = bike.getStatus();
        String owner = bike.getOwner();
        appendToCSV(csvFilePath, id, x, y, status, owner, time);
        return Result.success();
    }

    public static void appendToCSV(String csvFilePath, int id, String x, String y,
                                   String status, String owner, String time) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
            // 创建一个CSVWriter对象并传入FileWriter，以便将数据追加到CSV文件末尾

            // 追加数据行
            String[] data = {String.valueOf(id), x, y, status, owner, time};
            writer.writeNext(data);

            // 注意：每个数据行都需要使用字符串数组来表示
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id:
     * @return Result<?>
     * @author Shichao
     * @description 根据id删除单车
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteBikeViaId(@PathVariable Long id){
        System.out.println("news的删除功能调用");
        bikesMapper.deleteById(id);
        return Result.success();
    }

    /**
     * @param pageNum:
     * @param pageSize:
     * @param searchRider:
     * @return Result<?>
     * @author Shichao
     * @description dancehall信息打印
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String searchRider){
        System.out.println("打印表单");
        LambdaQueryWrapper<Bike> wrapper = Wrappers.<Bike>lambdaQuery();
        if (StrUtil.isNotBlank(searchRider)){
            wrapper.like(Bike::getOwner,searchRider);
        }
        Page<Bike> BikesPage = bikesMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(BikesPage);
    }


    private static final int PORT = 8080; // 服务器端口号
    ServerSocket serverSocket = null;
    @GetMapping("open_socket")
    public Result<?> openSocketListenerThread() throws Exception {
        if (serverSocket == null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        openServerSocket();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }
        return Result.success();
    }

    private void openServerSocket() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Waiting for clients...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // 创建一个新的线程来处理客户端连接
            Thread clientThread = new CsvThread(clientSocket);
            clientThread.start();
        }
    }


    private class CsvThread extends Thread {

        private String csvFilePath = "common/bike_positions.csv"; // 指定CSV文件路径
        private final Socket clientSocket;

        public CsvThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void appendToCSV(String csvFilePath, Bike bike,String time) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath, true))) {
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
                // 获取输入流
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
                    // 接收客户端发送的 Bike 对象
                    Bike person = (Bike) inputStream.readObject();
                    String time = (String) inputStream.readObject();

                    // 将 Person 对象的信息写入 CSV 文件
                    appendToCSV(csvFilePath ,person, time);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
