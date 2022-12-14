package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper extends BaseMapper<Book> {
    List<Book> findByUserId(@Param("userId") Integer userId);
}
