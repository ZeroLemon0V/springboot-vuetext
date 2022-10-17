package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;

    @Resource
    NewsMapper NewsMapper;


    @PostMapping
    public Result<?> save(@RequestBody News news){
        news.setTime(new Date());
        newsService.save(news);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody News news){
        newsService.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        System.out.println(id);
        newsService.removeById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<News> like = Wrappers.<News>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            like.like(News::getTitle, search);
        }
        Page<News> page = newsService.page(new Page<>(pageNum, pageSize), like);
        return Result.success(page);
    }

}
