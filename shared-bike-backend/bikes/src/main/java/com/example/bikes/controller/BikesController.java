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
    String csvFilePath = "common/bike_positions.csv"; // 指定CSV文件路径
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
}
