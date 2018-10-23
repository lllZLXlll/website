package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.Community;
import com.wchm.website.entity.Notice;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.NoticeService;
import com.wchm.website.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "关注人数")
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private AdminService adminService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "关注人数查询", response = Community.class)
    @UnToken
    public Community community(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return adminService.queryCommunity();
    }
}