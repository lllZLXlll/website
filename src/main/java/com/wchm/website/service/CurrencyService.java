package com.wchm.website.service;

import com.wchm.website.entity.Currency;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户币池service
 */

@Service
public interface CurrencyService {
    // 前端查询用户的锁仓信息，提现记录
    Result queryCurrencyAccount(String token, String language);

    //分页
    Result queryCurrencyByPage(Integer pageNum, Integer pageSize, String user_name);

    //保存
    Result currencySave(Currency currency);

    //删除
    Result delCurrencyByID(Integer id);

    //
    ModelAndView currencyInfo(Integer id);

    //修改
    Result currencyUpdate(Currency currency);

    // 导入用户excel
    void excelImport(Currency currency);

    // 查询带币池转账列表跳转
    ModelAndView recordInfo(Integer id);

    // 带币池转账列表数据
    Result queryCurrencyRecordByPage(Integer pageNum, Integer pageSize, Integer id);

    // 转账
    Result currencyTransfer(Long id, BigDecimal money) throws RuntimeException;

    // 用户提现申请
    Result extractApplyfor(String token, String address);

    // 用户提现申请列表
    Result queryApplyforByPage(Integer pageNum, Integer pageSize);

    // 管理员确认提现
    Result applyforSubmit(Integer id);

    // 查询代币池所有数据
    List<Currency> queryPoolList();

    Long updateCurrency(Currency item);
}
