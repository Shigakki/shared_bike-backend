package com.example.users.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.Result;
import com.common.config.JwtUtil;
import com.common.entity.User;
import com.example.users.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;



@RestController
@RequestMapping("/user")
@Api(value = "user-controller")

public class UserController {
    @Resource
    UserMapper userMapper;


    /**
     * @param user:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @PostMapping
    @ApiOperation(value = "添加用户")
    public Result<?> save(@RequestBody User user){
        System.out.println("网关调用添加用户");
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }

    /**
     * @param user:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @PutMapping
    @ApiOperation(value = "更新用户")
    public Result<?> update(@RequestBody User user){
        System.out.println("网关调用更新用户");
        userMapper.updateById(user);
        return Result.success();
    }

    /**
     * @param id:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    public Result<?> delete(@PathVariable Long id){
        System.out.println("网关调用删除用户");
        userMapper.deleteById(id);
        return Result.success();
    }

    /**
     * @param id:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @GetMapping ("/{id}")
    @ApiOperation(value = "根据用户的id查询用户")
    public Result<?> getById(@PathVariable Long id){
        System.out.println("调用根据ID查询");
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

    /**
     * @param pageNum:
     * @param pageSize:
     * @param searchName:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @GetMapping
    @ApiOperation(value = "进行分页查询")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String searchName){
        System.out.println("打印表单！");
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(searchName)){
            wrapper.like(User::getUsername,searchName);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    /**
     * 登录与注册
     * */
    /**
     * @param user:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<?> login(@NotNull @RequestBody User user){//这只是传进来的user，并没有在数据库中进行查找
        System.out.println("<<<<<<consumer调用了登录" + "穿过来的对象是：" + user);
        System.out.println("<<<<<<consumer" + "名字为" + user.getUsername());
        System.out.println("<<<<<<consumer" + "密码为" + user.getPassword());

        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if (res==null){
            return Result.error("-1","用户名或密码错误");
        }
        String token = JwtUtil.createJWT(UUID.randomUUID().toString(), user.getUsername(), null);
        System.out.println(">>>>>>完成了登录.");
        res.setToken(token);
        Result<?> result = Result.success(res);
        System.out.println("---->这次登录的token:"+res.getUsername()+token+"\n");
        return result;
    }

    /**
     * @param user:
     * @return Result<?>
     * @author Shichao
     * @description TODO
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if (res!=null){
            return Result.error("-1","用户名重复");
        }
        // 这里相当于就是把用户名和密码进行了设置不为空
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }
}
