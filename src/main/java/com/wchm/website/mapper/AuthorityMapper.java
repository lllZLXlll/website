package com.wchm.website.mapper;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.shiro.Role;
import com.wchm.website.qo.AdminQo;
import com.wchm.website.vo.AdminVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AuthorityMapper {

    @Select("<script> " +
            "   SELECT * FROM website_admin " +
            "   WHERE 1 = 1 " +
            "   <if test='username != null and username != \"\"'> " +
            "       AND username LIKE '%' #{username} '%'" +
            "   </if> " +
            "   <if test='mobile != null and mobile != \"\"'> " +
            "       AND mobile LIKE '%' #{mobile} '%'" +
            "   </if> " +
            "   ORDER BY id DESC " +
            "</script>")
    List<Admin> queryUsersData(@Param("username") String username, @Param("mobile") String mobile);

    @Select("SELECT * FROM website_role ORDER BY id")
    List<Role> queryRoleData();

    @Insert("INSERT INTO website_admin(" +
            "   username, password, mobile, state, create_time" +
            ") VALUES(" +
            "   #{adminQo.username}, #{adminQo.password}, #{adminQo.mobile}, #{adminQo.state}, NOW()" +
            ")")
    // 返回刚插入的数据自动生成的自增id
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "adminQo.id", before = false, resultType = Long.class)
    Long saveAdmin(@Param("adminQo") AdminQo adminQo);

    @Insert("INSERT INTO website_admin_role(" +
            "   uid, rid" +
            ") VALUES(" +
            "   #{adminId}, #{roleId}" +
            ")")
    Long saveAdminRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    @Select("SELECT * FROM website_admin WHERE id = #{id}")
    AdminVo queryUserInfo(@Param("id") Long id);

    @Select("SELECT rid FROM website_admin_role WHERE uid = #{uid} LIMIT 1")
    Long queryUserRoleId(@Param("uid") Long id);

    @Update("UPDATE website_admin SET username = #{adminQo.username}, " +
            "   mobile = #{adminQo.mobile}, state = #{adminQo.state} " +
            "WHERE id = #{adminQo.id}")
    Long userUpdate(@Param("adminQo") AdminQo adminQo);

    @Update("UPDATE website_admin_role SET" +
            "   rid = #{roleId} " +
            "WHERE uid = #{adminId}")
    Long updateAdminRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    @Insert("INSERT INTO website_role(" +
            "   rolename, roledesc" +
            ") VALUES(" +
            "   #{rolename}, #{roledesc}" +
            ")")
    Long saveRole(@Param("rolename") String rolename, @Param("roledesc") String roledesc);

    @Select("SELECT * FROM website_role WHERE id = #{id}")
    Role queryRoleInfo(@Param("id") Long id);

    @Update("UPDATE website_role SET" +
            "   rolename = #{rolename}, roledesc = #{roledesc} " +
            "WHERE id = #{id}")
    Long roleUpdate(@Param("id") Long id, @Param("rolename") String rolename, @Param("roledesc") String roledesc);
}
