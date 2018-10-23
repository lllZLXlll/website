package com.wchm.website.mapper;

import com.wchm.website.entity.CurrencyRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 客户币池明细表
 */
public interface CurrencyRecordMapper {

    //插入
    @Insert("INSERT INTO website_currency_pool_record(" +
            "   pool_id, `from`, `to`, `tx_address`, currency, periods, state, `describe`, admin, time" +
            ") " +
            "VALUES(" +
            "   #{record.pool_id}, #{record.from}, #{record.to}, #{record.txAddress}," +
            "   #{record.currency}, #{record.periods}, #{record.state}," +
            "   #{record.describe}, #{record.admin}, NOW()" +
            ")")
    Integer recordSave(@Param("record") CurrencyRecord record);

    @Select("SELECT IFNULL(max(periods),0) FROM website_currency_pool_record WHERE pool_id = #{id} AND state = 1")
    Integer queryCurrencyRecordMaxPeriods(@Param("id") Long id);
}

