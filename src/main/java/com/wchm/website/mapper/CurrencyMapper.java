package com.wchm.website.mapper;

import com.wchm.website.entity.Currency;
import com.wchm.website.entity.CurrencyRecord;
import com.wchm.website.entity.ExtractApplyfor;
import com.wchm.website.vo.CurrencyAccountVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 客户币池
 */
public interface CurrencyMapper {

    @Select("SELECT * FROM website_currency_pool WHERE state = 1 ORDER BY id DESC")
    List<Currency> queryCurrencyByPage();

    //查询
    @Select("SELECT * FROM website_currency_pool WHERE user_name LIKE '%' #{user_name} '%' AND state = 1 ORDER BY id DESC")
    List<Currency> queryCurrencyByPageName(@Param("user_name") String user_name);

    //删除
    @Delete("DELETE FROM website_currency_pool WHERE id = #{id}")
    Long delCurrencyByID(@Param("id") Integer id);

    //插入
    @Insert("INSERT INTO website_currency_pool(user_name, mobile, address, currency, surplus, lock_describe, remarks, create_time) " +
            "VALUES(#{currency.user_name},#{currency.mobile}, #{currency.address}," +
            "#{currency.currency},#{currency.surplus},#{currency.lock_describe}," +
            "#{currency.remarks}, NOW())")
    Long currencySave(@Param("currency") Currency currency);


    @Select("SELECT * FROM website_currency_pool WHERE id = #{id} AND state = 1")
    Currency currencyInfo(@Param("id") Integer id);

    //修改
    @Update(" UPDATE website_currency_pool SET " +
            " surplus = #{currency.surplus}" +
            " where id = #{currency.id} ")
    Long currencyUpdate(@Param("currency") Currency currency);

    @Select("SELECT * FROM website_currency_pool WHERE address = #{address} AND state = 1")
    Currency queryCurrencyByAddress(@Param("address") String address);

    @Update(" UPDATE website_currency_pool SET " +
            " currency = currency + #{currency.currency}, user_name = #{currency.user_name}," +
            " surplus = surplus + #{currency.surplus}, lock_describe = #{currency.lock_describe}," +
            " mobile = #{currency.mobile}, remarks = #{currency.remarks}" +
            " where address = #{currency.address} ")
    Long currencyUpdateByAddress(@Param("currency") Currency currency);

    @Select("SELECT * FROM website_currency_pool_record WHERE pool_id = #{id} ORDER BY id DESC")
    List<CurrencyRecord> queryCurrencyRecordByPage(@Param("id") Integer id);

    @Select("SELECT * FROM website_currency_pool WHERE id = #{id} AND state = 1 ORDER BY id DESC")
    Currency queryCurrencyById(@Param("id") Long id);

    @Select("SELECT *, " +
            "   CASE  " +
            "       WHEN LENGTH(new_address) < 42 THEN " +
            "           old_address " +
            "   ELSE " +
            "       new_address " +
            "   END address " +
            "FROM ( " +
            "   SELECT " +
            "       currency, " +
            "       surplus, " +
            "       proportion, " +
            "       CONCAT(DATE_FORMAT(lock_begin_time, '%Y.%m.%d'), '-', DATE_FORMAT(lock_end_time, '%Y.%m.%d')) lock_time, " +
            "       lock_describe, " +
            "       t1.address old_address, " +
            "       t2.address new_address " +
            "   FROM " +
            "       website_currency_pool t1 " +
            "   JOIN eacoo_users t2 ON t1.mobile = t2.mobile " +
            "   WHERE t2.uid = #{id} " +
            ") t")
    CurrencyAccountVo queryCurrencyAccount(@Param("id") Long userId);

    @Select("SELECT * FROM website_extract_applyfor WHERE uid = #{uid} AND state = 1 ")
    List<ExtractApplyfor> queryApplyforListByUid(@Param("uid") Long userId);
}

