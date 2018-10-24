package com.wchm.website.mapper;

import com.wchm.website.entity.Community;
import com.wchm.website.entity.Team;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommunityMapper {


    @Select("SELECT * FROM website_community_follow_count")
    List<Community> queryCommunity();

    @Select("SELECT * FROM website_community_follow_count WHERE id = #{id}")
    Community communityInfo(@Param("id") Integer id);

    @Select("SELECT * FROM website_community_follow_count")
    List<Community> queryCommunityByPage();

    // 删除
    @Delete("DELETE FROM website_community_follow_count WHERE id = #{id}")
    Long delCommunityByID(@Param("id") Integer id);


    // 插入
    @Insert("INSERT INTO website_community_follow_count(english_name, description, link, follow_number, state) " +
            "VALUES(#{community.english_name}, #{community.description}, #{community.link}, #{community.follow_number}, #{community.state})")
    Long communitySave(@Param("community") Community community);

    // 修改
    @Update("UPDATE website_community_follow_count" +
            " SET english_name = #{community.english_name}" + " ,description = #{community.description}" +
            " ,link = #{community.link}" + " ,follow_number = #{community.follow_number}" + " ,state = #{community.state}" +
            " WHERE id = #{community.id}")
    Long communityUpdate(@Param("community") Community community);
}
