package com.wchm.website.controller;

import com.wchm.website.entity.Partner;
import com.wchm.website.service.PartnerService;
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

@Api(tags = "合作伙伴")
@RestController
@RequestMapping("/partner")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "合作伙伴查询", response = Partner.class)
    public Object partner(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return partnerService.queryPartner();
    }

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "合作伙伴分页查询", response = Partner.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "query")
    })
    public Object messageList(HttpServletResponse response, Integer pageNum,
                              Integer pageSize) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return partnerService.queryPartnerList(pageNum, pageSize);
    }

}
