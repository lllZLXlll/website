package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wchm.website.entity.Admin;
import com.wchm.website.entity.shiro.Role;
import com.wchm.website.mapper.AuthorityMapper;
import com.wchm.website.qo.AdminQo;
import com.wchm.website.service.AuthorityService;
import com.wchm.website.util.Result;
import com.wchm.website.vo.AdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * 官网区块链浏览器模块所有接口的实现写在此类下
 */

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AuthorityMapper authorityMapper;

    /**
     * 查询用户列表数据
     */
    @Override
    public Result queryUsersData(String username, String mobile, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Admin> data = authorityMapper.queryUsersData(username, mobile);
        PageInfo<Admin> p = new PageInfo(data);
        return Result.create().success(p);
    }

    @Override
    public ModelAndView addUser() {
        ModelAndView mav = new ModelAndView("user-add");

        List<Role> roleList = authorityMapper.queryRoleData();

        mav.getModel().put("roleList", roleList);
        return mav;
    }

    @Override
    public Result saveUser(AdminQo adminQo) {
        if (StringUtils.isEmpty(adminQo.getUsername())) {
            return Result.create().fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(adminQo.getPassword())) {
            return Result.create().fail("密码不能为空");
        }
        if (StringUtils.isEmpty(adminQo.getMobile())) {
            return Result.create().fail("电话号码不能为空");
        }
        if (adminQo.getRoleId() == null) {
            return Result.create().fail("角色不能为空");
        }

        // 增加一个用户
        authorityMapper.saveAdmin(adminQo);
        if (adminQo.getId() == null) {
            return Result.create().fail("添加用户失败");
        }

        // 增加此用户与对应角色的关联
        long result = authorityMapper.saveAdminRole(adminQo.getId(), adminQo.getRoleId());
        if (result <= 0) {
            return Result.create().fail("添加用户角色关系失败");
        }

        return Result.create().success("添加用户成功");
    }

    @Override
    public ModelAndView userInfo(Long id) {
        ModelAndView mav = new ModelAndView("user-edit");

        // 基本信息
        AdminVo adminVo = authorityMapper.queryUserInfo(id);

        // 关联的角色id
        Long roleId = authorityMapper.queryUserRoleId(adminVo.getId());

        // 角色列表
        List<Role> roleList = authorityMapper.queryRoleData();

        adminVo.setRoleList(roleList);
        adminVo.setRoleId(roleId);

        mav.getModel().put("adminVo", adminVo);
        return mav;
    }

    @Override
    public Result userUpdate(AdminQo adminQo) {
        if (StringUtils.isEmpty(adminQo.getUsername())) {
            return Result.create().fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(adminQo.getMobile())) {
            return Result.create().fail("电话号码不能为空");
        }
        if (adminQo.getRoleId() == null) {
            return Result.create().fail("角色不能为空");
        }

        long result = authorityMapper.userUpdate(adminQo);
        if (result <= 0) {
            return Result.create().fail("修改用户信息失败");
        }

        // 增加此用户与对应角色的关联
        result = authorityMapper.updateAdminRole(adminQo.getId(), adminQo.getRoleId());
        if (result <= 0) {
            return Result.create().fail("修改用户角色关系失败");
        }

        return Result.create().success("修改成功");
    }

    /**
     * 查询角色列表数据
     */
    @Override
    public Result queryRoleData(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Role> data = authorityMapper.queryRoleData();
        PageInfo<Admin> p = new PageInfo(data);
        return Result.create().success(p);
    }

    @Override
    public Result saveRole(String rolename, String roledesc) {
        if (StringUtils.isEmpty(rolename)) {
            return Result.create().fail("角色名称不能为空");
        }
        if (StringUtils.isEmpty(roledesc)) {
            return Result.create().fail("角色描述不能为空");
        }

        // 增加一个角色
        long result = authorityMapper.saveRole(rolename, roledesc);
        if (result <= 0) {
            return Result.create().fail("添加角色失败");
        }

        return Result.create().success("添加角色成功");
    }

    @Override
    public ModelAndView roleInfo(Long id) {
        ModelAndView mav = new ModelAndView("role-edit");

        // 基本信息
        Role role = authorityMapper.queryRoleInfo(id);

        mav.getModel().put("role", role);
        return mav;
    }

    @Override
    public Result roleUpdate(Long id, String rolename, String roledesc) {
        if (StringUtils.isEmpty(rolename)) {
            return Result.create().fail("角色名称不能为空");
        }
        if (StringUtils.isEmpty(roledesc)) {
            return Result.create().fail("角色描述不能为空");
        }
        long result = authorityMapper.roleUpdate(id, rolename, roledesc);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }

        return Result.create().success("修改成功");
    }
}
