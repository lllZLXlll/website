package com.wchm.website.controller;

import com.wchm.website.service.CurrencyService;
import com.wchm.website.util.Result;
import com.wchm.website.vo.CurrencyAccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, paramType = "query"),
            @ApiImplicitParam(name = "language", value = "en:英文版，zh:中文版", required = true, paramType = "query"),
    })
    public Result queryCurrencyAccount(HttpServletResponse response, @RequestParam("token") String token,
                                       @RequestParam(value = "language", defaultValue = "zh") String language) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // 防止跨域
        try {
           return currencyService.queryCurrencyAccount(token, language);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.create().fail("网络异常");
        }
    }

    /**
     * 前端用户提现申请
     *
     * @param response
     * @param token
     * @param address
     * @return
     */
    @GetMapping("/applyfor")
    @ResponseBody
    @ApiOperation(value = "官网个人中心，用户提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户token", required = true, paramType = "query"),
            @ApiImplicitParam(name = "address", value = "用户钱包地址", required = true, paramType = "query"),
    })
    public Result extractApplyfor(HttpServletResponse response, String token,
                                  String address) {
        response.setHeader("Access-Control-Allow-Origin", "*"); // 防止跨域
        try {
           return currencyService.extractApplyfor(token, address);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.create().fail("提交申请异常");
        }
    }
}
