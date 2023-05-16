package com.example.bikes.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.Result;
import com.common.entity.Bike;
import com.example.bikes.mapper.BikesMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/bikes")
public class BikesController {
    @Resource
    BikesMapper bikesMapper;

    /**
     * @param bike:
     * @return Result<?>
     * @author Shichao
     * @description TODO
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
     * @description TODO
     */
    @PutMapping
    public Result<?> updateBike(@RequestBody Bike bike){
        System.out.println("更新单车信息");
        bikesMapper.updateById(bike);
        return Result.success();
    }

    /**
     * @param id:
     * @return Result<?>
     * @author Shichao
     * @description TODO
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
     * @description TODO
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
