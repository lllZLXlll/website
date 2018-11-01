package com.wchm.website.controller;

import com.wchm.website.service.CurrencyService;
import com.wchm.website.util.Result;
import com.wchm.website.vo.CurrencyAccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "客户代币池")
@RestController
@RequestMapping("/currency")
public class CurrencyController {

    public final static Logger log = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    private CurrencyService currencyService;


    /**
     * 前端查询用户的锁仓信息，提现记录
     *
     * @param response
     * @param token
     * @return
     */
    @GetMapping("/account")
    @ResponseBody
    @ApiOperation(value = "官网个人中心，锁仓信息查询", response = CurrencyAccountVo.class)
    public Result queryCurrencyAccount(HttpServletResponse response, @RequestParam("token") String token) {
        response.setHeader("Access-Control-Allow-Origin", "*");//防止跨域
        try {
           return currencyService.queryCurrencyAccount(token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.create().fail("网络异常");
        }
    }
}
