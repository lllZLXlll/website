package com.wchm.website.controller;

import com.wchm.website.entity.Community;
import com.wchm.website.service.CommunityService;
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

@Api(tags = "关注")
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "关注人数查询", response = Community.class)
    public Object partner(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return communityService.queryCommunity();
    }

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "关注人数分页查询", response = Community.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "query")
    })
    public Object messageList(HttpServletResponse response, Integer pageNum,
                              Integer pageSize) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return communityService.queryCommunityByPage(pageNum, pageSize);
    }
}
