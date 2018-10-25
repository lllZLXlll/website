package com.wchm.website.entity.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 权限类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private int id;
    private String modelname;
    private String permission;

    private List<Role> roleList;//角色权限关系   多对多
}
