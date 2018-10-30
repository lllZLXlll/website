package com.wchm.website.mapper;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.entity.shiro.Permission;
import com.wchm.website.entity.shiro.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdminMapper {

    @Select("SELECT * FROM website_admin WHERE username = #{username}")
    Admin queryUserNameAndPwd(@Param("username") String username);

    @Select("SELECT count(id) FROM website_news WHERE state = 1")
    Integer queryNewsCount();

    @Select("SELECT count(id) FROM website_notice WHERE state = 1")
    Integer queryNoticeCount();

    /**
     * 一对多查询
     *
     * @param username
     * @return
     */
    @Select("SELECT * " +
            "FROM website_admin " +
            "WHERE username = #{username} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "create_time", column = "create_time"),
            @Result(
                    property = "roleList", column = "id",
                    many = @Many(select = "com.wchm.website.mapper.AdminMapper.findRoleByAdminId")
            )
    })
    Admin findAdminByName(@Param("username") String username);

    @Select("SELECT t2.* FROM website_admin_role t1 JOIN website_role t2 ON t1.rid = t2.id WHERE t1.uid = #{uid}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "rolename", column = "rolename"),
            @Result(property = "roledesc", column = "roledesc"),
            @Result(
                    property = "permissionList", column = "id",
                    many = @Many(select = "com.wchm.website.mapper.AdminMapper.findPermissionByRoleId")
            )
    })
    List<Role> findRoleByAdminId(@Param("uid") Long uid);

    @Select("SELECT t2.* FROM website_role_permission t1 JOIN website_permission t2 ON t1.pid = t2.id WHERE rid = #{rid}")
    List<Permission> findPermissionByRoleId(@Param("rid") Long rid);

}
