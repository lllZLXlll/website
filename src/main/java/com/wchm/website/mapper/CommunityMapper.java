package com.wchm.website.mapper;

import com.wchm.website.entity.Community;
import com.wchm.website.entity.Team;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommunityMapper {


    @Select("SELECT * FROM website_community_follow_count LIMIT 1")
    Community queryCommunity();

    @Select("SELECT * FROM website_community_follow_count WHERE id = #{id}")
    Community communityInfo(@Param("id") Integer id);

    @Update("UPDATE website_community_follow_count" +
            " SET count1 = #{community.count1}" +
            " ,count2 = #{community.count2}" +
            " ,count3 = #{community.count3}" +
            " ,count4 = #{community.count4}" +
            " WHERE id = #{community.id}")
    Long communityUpdate(@Param("community") Community community);
}
