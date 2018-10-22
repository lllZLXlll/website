package com.wchm.website.mapper;

import com.wchm.website.entity.Operation;
import com.wchm.website.entity.Partner;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 操作日志SQL
 */
public interface OperationMapper {

    //接口查询
    @Select("SELECT * FROM website_operation_log WHERE state = 1 ORDER BY create_time DESC LIMIT 3")
    List<Operation> queryOperation();
    //
    @Select("SELECT * FROM website_operation_log ORDER BY create_time DESC")
    List<Operation> queryOperationByPage();

    //分页、按条件查询
    @Select("SELECT * FROM website_operation_log WHERE admin_name LIKE '%' #{admin_name} '%' ORDER BY create_time DESC")
    List<Operation> queryOperationByPageName(@Param("admin_name") String admin_name);

    //删除
    @Delete("DELETE FROM website_operation_log WHERE id = #{id}")
    Long delOperationByID(@Param("id") Integer id);

    //插入
    @Insert("INSERT INTO website_operation_log(admin_name, operation_type, money, address, create_time, state) " +
            "VALUES(#{operation.admin_name}, #{operation.operation_type}, #{operation.money}," +
            " #{operation.address}, #{operation.create_time},#{operation.state})")
    Long operationSave(@Param("operation") Operation operation);


    @Select("SELECT * FROM website_operation_log WHERE id = #{id}")
    Operation operationInfo(@Param("id") Integer id);

    //修改
    @Update(" UPDATE website_operation_log SET " +
            "  admin_name = #{website_operation_log.admin_name},operation_type = #{operation.operation_type}, " +
            " money = #{operation.money}, address = #{operation.address}, " +
            " state = #{operation.state},create_time = #{operation.create_time} " +
            " where id = #{operation.id} ")
    Long operationUpdate(@Param("operation") Operation operation);

    @Select("SELECT * FROM website_operation_log WHERE id = #{id}")
    Partner queryOperationInfo(@Param("id") Long id);
}
