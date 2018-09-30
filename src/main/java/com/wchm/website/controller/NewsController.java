package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.News;
import com.wchm.website.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "新闻")
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "首页获取新闻", response = News.class)
    @UnToken
    public Object news(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return newsService.queryNews();
    }

    @GetMapping("/info")
    @ResponseBody
    @ApiOperation(value = "新闻", response = News.class)
    @UnToken
    public Object newsInfo(HttpServletResponse response,Long id) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return newsService.newsInfoo(id);
    }



}
