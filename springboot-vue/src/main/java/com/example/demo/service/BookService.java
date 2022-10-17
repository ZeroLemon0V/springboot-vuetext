package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> findByUserId(@Param("userId") Integer userId);
}
