package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.Notice;
import com.wchm.website.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "公告")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "获取公告", response = Notice.class)
    @UnToken
    public Object notices(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return noticeService.queryNotices();
    }

    @GetMapping("/info")
    @ResponseBody
    @ApiOperation(value = "公告", response = Notice.class)
    @UnToken
    public Object noticeInfo(Long id, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return noticeService.queryNoticeInfo(id);
    }

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "公告分页查询", response = Notice.class)
    @UnToken
    public Object noticeList(HttpServletResponse response,Integer pageNum, Integer pageSize) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return noticeService.queryNoticeList(pageNum,pageSize);
    }
}