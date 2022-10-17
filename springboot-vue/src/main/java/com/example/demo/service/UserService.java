package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserService extends IService<User> {
    Page<User> findPage(Page<User> page, @Param("nickName") String nickName);
}
