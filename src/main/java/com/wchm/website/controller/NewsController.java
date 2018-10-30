package com.wchm.website.controller;

import com.wchm.website.entity.News;
import com.wchm.website.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @GetMapping("")
//    @ResponseBody
//    @ApiOperation(value = "首页获取新闻", response = News.class)
//    public Object news(HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        return newsService.queryNews();
//    }
//
//    @GetMapping("/info")
//    @ResponseBody
//    @ApiOperation(value = "新闻", response = News.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "lang", value = "1.英文版，0.中文版", required = true,paramType = "query"),
//            @ApiImplicitParam(name = "id", value = "ID", required = true,paramType = "query"),
//    })
//    public Object newsInfo(HttpServletResponse response,Long id,Integer lang) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        return newsService.newsInfoo(id,lang);
//    }


    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "新闻分页查询", response = News.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lang", value = "1.英文版，0.中文版", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "query")
    })
    public Object noticeList(HttpServletResponse response, Integer pageNum,
                             Integer pageSize, Integer lang) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return newsService.queryNewsList(pageNum, pageSize, lang);
    }

}
