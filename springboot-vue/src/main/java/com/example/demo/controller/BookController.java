package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService BookService;

    @Resource
    BookMapper BookMapper;


    @PostMapping
    public Result<?> save(@RequestBody Book Book){
        BookService.save(Book);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Book Book){
        BookService.updateById(Book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        System.out.println(id);
        BookService.removeById(id);
        return Result.success();
    }

    @PostMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        BookService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{userId}")
    public Result<?> getByUserId(@PathVariable Integer userId) {
        return Result.success(BookService.findByUserId(userId));
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Book> like = Wrappers.<Book>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            like.like(Book::getName, search);
        }
//        Page<Book> page = BookMapper.selectPage(new Page<>(pageNum, pageSize),like );
        Page<Book> page = BookService.page(new Page<>(pageNum, pageSize), like);

        return Result.success(page);
    }

}
