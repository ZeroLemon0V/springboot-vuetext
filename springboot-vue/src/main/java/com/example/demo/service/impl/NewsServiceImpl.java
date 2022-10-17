package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Book;
import com.example.demo.entity.News;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.service.BookService;
import com.example.demo.service.NewsService;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
