package com.wchm.website.mapper;

import com.wchm.website.entity.News;
import com.wchm.website.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NoticeMapper {

    @Select("SELECT * FROM website_notice WHERE state = 1 ORDER BY time DESC LIMIT 3")
    List<Notice> queryNotices();

    @Select("SELECT * FROM website_notice ORDER BY create_time DESC")
    List<Notice> queryNoticeByPage();

    @Select("<script> " +
            "SELECT  * FROM website_notice" +
            " WHERE title LIKE '%' #{title} '%' <choose> <when  test='lang != null'> and lang = #{lang}</when>  </choose> " +
            "ORDER BY time DESC " +
            "</script>")
    List<Notice> queryNoticeByPageTitle(@Param("title") String title,@Param("lang") Integer lang);

    @Insert("INSERT INTO website_notice(title, content, time, description, create_time, state,lang) " +
            "VALUES(#{notice.title}, #{notice.content}, #{notice.timeInsert}, #{notice.description}, NOW(), #{notice.state},#{notice.lang})")
    Long noticeSave(@Param("notice") Notice notice);

    @Delete("DELETE FROM website_notice WHERE id = #{id}")
    Long delNoticeByID(@Param("id") Integer id);

    @Select("SELECT * FROM website_notice WHERE id = #{id}")
    Notice noticeInfo(Integer id);

    @Update(" UPDATE website_notice SET " +
            " title = #{notice.title}, content = #{notice.content}, description = #{notice.description}," +
            " time = #{notice.timeInsert}, state = #{notice.state} ,lang = #{notice.lang}" +
            " where id = #{notice.id} ")
    Long newsUpdate(@Param("notice") Notice notice);

    @Select("SELECT * FROM website_notice WHERE id = #{id} and lang = #{lang}")
    Notice queryNoticeInfo(@Param("id") Long id,@Param("lang") Integer lang);


    @Select("SELECT * FROM website_notice WHERE state = 1 and lang = #{lang} ORDER BY time DESC")
    List<Notice> queryNoticeList(@Param("lang") Integer lang);

}
