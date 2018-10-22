package com.wchm.website.mapper;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Community;
import com.wchm.website.entity.News;
import com.wchm.website.entity.Operation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminMapper {

    @Select("SELECT * FROM website_admin WHERE username = #{username}")
    Admin queryUserNameAndPwd(@Param("username") String username);

    @Select("SELECT count(id) FROM website_news WHERE state = 1")
    Integer queryNewsCount();

    @Select("SELECT count(id) FROM website_notice WHERE state = 1")
    Integer queryNoticeCount();

    //插入
    @Insert("INSERT INTO website_operation_log(admin_name, operation_type, money, address, create_time, state) " +
            "VALUES(#{operation.admin_name}, #{operation.operation_type}, #{operation.money}," +
            " #{operation.address}, #{operation.create_time},#{operation.state})")
    Long operationSave(@Param("operation") Operation operation);


}
