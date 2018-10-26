package com.wchm.website.mapper;

import com.wchm.website.entity.Team;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TeamMapper {

    /*@Select("SELECT * FROM website_team WHERE state = 1 ORDER BY create_time DESC LIMIT 3")
    List<Team> queryTeam();

    @Select("SELECT * FROM team WHERE id = #{id}")
    Team queryteamInfo(@Param("id") Long id);*/

    @Select("SELECT * FROM website_team WHERE state = 1 ORDER BY create_time DESC ")
    List<Team> queryTeam();

    @Select("SELECT * FROM website_team ORDER BY create_time DESC")
    List<Team> queryTeamByPage();


    //查询
    @Select("SELECT * FROM website_team WHERE team_name LIKE '%' #{team_name} '%' ORDER BY create_time DESC")
    List<Team> queryTeamByPageTitle(@Param("team_name") String team_name);

    //删除
    @Delete("DELETE FROM website_team WHERE id = #{id}")
    Long delTeamByID(@Param("id") Integer id);

    //插入
    @Insert("INSERT INTO website_team(number, team_name, description, head, state, create_time) " +
            "VALUES(#{team.number}, #{team.team_name}, #{team.description}, #{team.head}, #{team.state},#{team.create_time})")
    Long teamSave(@Param("team") Team team);

    @Select("SELECT * FROM website_team WHERE id = #{id}")
    Team teamInfo(@Param("id") Integer id);

    //修改
    @Update(" UPDATE website_team SET " +
            " number = #{team.number}, team_name = #{team.team_name}, " +
            " description = #{team.description}, head = #{team.head}, " +
            " state = #{team.state},create_time = #{team.create_time} " +
            " where id = #{team.id} ")
    Long teamUpdate(@Param("team") Team team);
}
