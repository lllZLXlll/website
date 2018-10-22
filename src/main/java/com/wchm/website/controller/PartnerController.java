package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.Partner;
import com.wchm.website.service.PartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "团队")
@RestController
@RequestMapping("/partner")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "首页获取团队", response = Partner.class)
    @UnToken
    public Object news(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return partnerService.queryPartner();
    }

}
