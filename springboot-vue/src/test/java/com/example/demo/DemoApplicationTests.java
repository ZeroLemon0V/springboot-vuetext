package com.example.demo;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	BookService BookService;

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		List<Book> list = BookService.list();
		System.out.println(list);
	}

	@Test
	void contextLoads1() {

		LambdaQueryWrapper<User> like = Wrappers.<User>lambdaQuery();
		String search = "张三";
		if (StrUtil.isNotBlank(search)){
			like.like(User::getNickName, search);
		}
		Page<User> page = userService.findPage(new Page<User>(1,10),search);
		System.out.println(page);
	}

	@Test
	void list(){
		List<User> list = userService.list();
		list.forEach(System.out::println);
	}

}
