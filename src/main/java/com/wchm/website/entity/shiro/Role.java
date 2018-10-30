package com.wchm.website.entity.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String rolename;//角色名称
    private String roledesc;//角色描述

    private List<Permission> permissionList;//角色权限关系  多对多  一个角色对应多个权限

}
