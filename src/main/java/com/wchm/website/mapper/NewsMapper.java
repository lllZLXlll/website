package com.wchm.website.mapper;

import com.wchm.website.entity.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NewsMapper {

    @Select("SELECT * FROM website_news WHERE state = 1 ORDER BY time DESC LIMIT 3")
    List<News> queryNews();

    @Select("SELECT * FROM website_news ORDER BY create_time DESC")
    List<News> queryNewsByPage();

    @Select("<script> " +
            "SELECT  * FROM website_news" +
            " WHERE title LIKE '%' #{title} '%' <when  test='lang != null'> and lang = #{lang}</when> " +
            "ORDER BY time DESC " +
            "</script>")
    List<News> queryNewsByPageTitle(@Param("title") String title,@Param("lang") Integer lang);

    @Delete("DELETE FROM website_news WHERE id = #{id}")
    Long delNewsByID(@Param("id") Integer id);

    @Insert("INSERT INTO website_news(title, content, time, icon, url, create_time, state,lang) " +
            "VALUES(#{news.title}, #{news.content}, #{news.timeInsert}, #{news.icon}, #{news.url}, NOW(), #{news.state}, #{news.lang})")
    Long newsSave(@Param("news") News news);

    @Select("SELECT * FROM website_news WHERE id = #{id}")
    News newsInfo(@Param("id") Integer id);

    @Update(" UPDATE website_news SET " +
            " title = #{news.title}, content = #{news.content}, " +
            " time = #{news.timeInsert}, icon = #{news.icon}, " +
            " url = #{news.url}, state = #{news.state} ,lang = #{news.lang}" +
            " where id = #{news.id} ")
    Long newsUpdate(@Param("news") News news);

    @Select("SELECT * FROM website_news WHERE id = #{id}")
    News queryNewsInfo(@Param("id") Long id);
}
