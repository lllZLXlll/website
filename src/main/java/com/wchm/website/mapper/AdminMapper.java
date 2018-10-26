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

    //插入
    @Insert("INSERT INTO website_operation_log(admin_name, operation_type, money, address, create_time, state) " +
            "VALUES(#{operation.admin_name}, #{operation.operation_type}, #{operation.money}," +
            " #{operation.address}, #{operation.create_time},#{operation.state})")
    Long operationSave(@Param("operation") Operation operation);


    /**
     * 一对多查询
     *
     * @param username
     * @return
     */
//    @Select("SELECT" +
//            "   a.id u_id, username, password, " +
//            "   r.id r_id, rolename, roledesc, " +
//            "   p.id p_id , modelname, permission " +
//            "FROM website_admin a " +
//            "   INNER JOIN website_admin_role ar ON a.id = ar.uid " +
//            "   INNER JOIN website_role r ON ar.rid = r.id " +
//            "   INNER JOIN website_role_permission rp ON r.id = rp.rid " +
//            "   INNER JOIN website_permission p ON rp.pid = p.id " +
//            "   WHERE a.username = #{username} ")

    @Select("SELECT * " +
            "FROM website_admin " +
            "WHERE username = #{username} ")
    @Results({
            @Result(
                    property = "roleList", column = "id",
                    many = @Many(select = "com.wchm.website.mapper.AdminMapper.findRoleByAdminId")
            )
    })
    Admin findAdminByName(@Param("username") String username);

    @Select("SELECT * FROM website_admin_role WHERE uid = #{uid}")
    @Results({
            @Result(
                    property = "permissionList", column = "id",
                    many = @Many(select = "com.wchm.website.mapper.AdminMapper.findPermissionByRoleId")
            )
    })
    List<Role> findRoleByAdminId(@Param("uid") Long uid);

    @Select("SELECT * FROM website_role_permission WHERE rid = #{rid}")
    List<Permission> findPermissionByRoleId(@Param("rid") Long rid);

}
