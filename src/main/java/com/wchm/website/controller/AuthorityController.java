package com.wchm.website.controller;

import com.wchm.website.qo.AdminQo;
import com.wchm.website.service.AuthorityService;
import com.wchm.website.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于权限的接口写在此类下，增加用户，增加角色...
 * <p>
 * TODO
 * 由于时间关系，现在的一个用户只有一个角色，以后有时间需要修改。
 * 也只控制了角色的权限，没有对具体某个功能模块做权限。
 * 增加、修改用户时能选择多个角色，或者多做一个功能，选择某个用户增加、修改角色权限。
 */

@Api(tags = "角色权限")
@Controller
@RequestMapping("/admin/authority")
public class AuthorityController {

    @Autowired
    AuthorityService authorityService;

    /**
     * 查询用户列表数据-跳转
     *
     * @return
     */
    @RequiresRoles(value = "admin") // 需要管理员权限
    @GetMapping("/user/list")
    @ApiOperation(value = "查询用户列表数据-跳转")
    public String queryUser() {
        return "user-list";
    }

    /**
     * 查询用户列表数据
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/user/data")
    @ResponseBody
    @ApiOperation(value = "查询用户列表数据")
    public Result queryUsersData(String username, String mobile, Integer pageNum, Integer pageSize) {
        try {
            return authorityService.queryUsersData(username, mobile, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("查询数据失败，请稍后重试");
        }
    }

    /**
     * 添加用户-跳转
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/user/add")
    @ApiOperation(value = "添加用户-跳转")
    public ModelAndView addUser() {
        return authorityService.addUser();
    }

    /**
     * 添加用户
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @PostMapping("/user/save")
    @ApiOperation(value = "添加用户")
    @ResponseBody
    public Result saveUser(@RequestBody AdminQo adminQo) {
        return authorityService.saveUser(adminQo);
    }

    /**
     * 编辑用户-跳转
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/user/info/{id}")
    @ApiOperation(value = "编辑用户-跳转")
    public ModelAndView userInfo(@PathVariable("id") Long id) {
        return authorityService.userInfo(id);
    }

    /**
     * 编辑用户
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @PostMapping("/user/update")
    @ApiOperation(value = "编辑用户")
    @ResponseBody
    public Result userUpdate(@RequestBody AdminQo adminQo) {
        return authorityService.userUpdate(adminQo);
    }

    // TODO 一下部分还没有做
    /**
     * 查询角色列表数据-跳转
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/role/list")
    @ApiOperation(value = "查询角色列表数据-跳转")
    public String queryRoleData() {
        return "role-list";
    }

    /**
     * 查询角色列表数据
     *
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/role/list")
    @ResponseBody
    @ApiOperation(value = "查询角色列表数据-跳转")
    public Result queryRole() {
        try {
            return authorityService.queryRoleData();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("查询数据失败，请稍后重试");
        }
    }


}