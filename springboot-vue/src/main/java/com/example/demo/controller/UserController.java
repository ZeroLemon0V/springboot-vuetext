package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Resource
    UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if (res==null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        if (user.getNickName()==null){
            user.setNickName("123456");
        }
        if (user.getRole()==null){
            user.setRole(2);
        }
        if (res==null){
            userService.save(user);
            return Result.success();
        }else {
            return Result.error("-1","该用户名已存在");
        }

    }

    @PostMapping
    public Result<?> save(@RequestBody User user){
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userService.save(user);
        System.out.println(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        System.out.println(id);
        userService.removeById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id){
        return Result.success(userService.getById(id));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> like = Wrappers.<User>lambdaQuery().orderByAsc(User::getId);
        if (StrUtil.isNotBlank(search)){
            like.like(User::getNickName, search);
        }
//        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize),like );
        Page<User> page = userService.findPage(new Page<>(pageNum, pageSize), search);
//        Page<User> page = userMapper.findPage(new Page<>(pageNum, pageSize), search);
//        page.getRecords().forEach(System.out::println);
        return Result.success(page);
    }

}
