package com.wchm.website.service;

import com.wchm.website.entity.Admin;
import com.wchm.website.qo.AdminQo;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于权限的接口写在此类下，增加用户，增加角色...
 */

@Service
public interface AuthorityService {
    /**
     * 查询用户列表数据
     */
    Result queryUsersData(String username, String mobile, Integer pageNum, Integer pageSize);

    /**
     * 查询角色列表数据
     */
    Result queryRoleData(Integer pageNum, Integer pageSize);

    /**
     * 添加跳转，查询所有角色数据
     */
    ModelAndView addUser();

    /**
     * 添加用户
     */
    Result saveUser(AdminQo adminQo);

    /**
     * 编辑用户-跳转-查询用户信息
     */
    ModelAndView userInfo(Long id);

    /**
     * 编辑用户
     */
    Result userUpdate(AdminQo adminQo);

    /**
     * 保存角色
     */
    Result saveRole(String rolename, String roledesc);

    /**
     * 编辑角色-跳转-查询角色信息
     */
    ModelAndView roleInfo(Long id);

    /**
     * 编辑角色
     */
    Result roleUpdate(Long id, String rolename, String roledesc);
}
