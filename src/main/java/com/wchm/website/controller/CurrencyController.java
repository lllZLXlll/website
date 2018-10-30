package com.wchm.website.controller;

import com.wchm.website.entity.Currency;
import com.wchm.website.service.CurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "客户币池")
@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "首页获取带币池页面", response = Currency.class)
    public Object team(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");//防止跨域
        return currencyService.queryCurrency();
    }
}
