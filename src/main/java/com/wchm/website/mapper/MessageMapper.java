package com.wchm.website.mapper;

import com.wchm.website.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MessageMapper {



    @Select("SELECT * FROM website_message WHERE state = 1 ORDER BY create_time DESC ")
    List<Message> queryMessage();


    @Select("SELECT * FROM website_message WHERE id = #{id}")
    Message messageInfo(@Param("id") Integer id);

/*    @Select("SELECT * FROM website_message WHERE id = #{id}")
    Message queryMessageInfo(@Param("id") Long id);*/

/*    @Select("SELECT * FROM website_message WHERE state = 1 and title = #{title} ORDER BY create_time DESC")
    List<Message> queryMessageList(@Param("title") String title);*/

    @Select("SELECT * FROM website_message ORDER BY create_time DESC")
    List<Message> queryMessageByPage();

    //查询
    @Select("SELECT * FROM website_message WHERE title LIKE '%' #{title} '%' ORDER BY create_time DESC")
    List<Message> queryMessageByPageTitle(@Param("title") String title);

    //删除
    @Delete("DELETE FROM website_message WHERE id = #{id}")
    Long delMessageByID(@Param("id") Integer id);

    //插入
    @Insert("INSERT INTO website_message(title, content, state, create_time) " +
            "VALUES(#{message.title}, #{message.content}, #{message.state},#{message.create_time})")
    Long messageSave(@Param("message") Message message);

    //修改
    @Update(" UPDATE website_message SET " +
            " title = #{message.title}, content = #{message.content}, " +
            "  state = #{message.state}, " + " create_time = #{message.create_time} " +
            " where id = #{message.id} ")
    Long messageUpdate(@Param("message") Message message);

 }
