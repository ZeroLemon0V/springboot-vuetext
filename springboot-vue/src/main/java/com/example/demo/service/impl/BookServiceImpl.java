package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper,Book> implements BookService {

    @Override
    public List<Book> findByUserId(Integer userId) {
        List<Book> byUserId = this.getBaseMapper().findByUserId(userId);
        return byUserId;
    }
}
