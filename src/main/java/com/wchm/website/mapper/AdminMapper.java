package com.wchm.website.mapper;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.News;
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

}
