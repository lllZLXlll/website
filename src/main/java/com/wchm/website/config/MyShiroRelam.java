package com.wchm.website.config;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.shiro.Permission;
import com.wchm.website.entity.shiro.Role;
import com.wchm.website.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyShiroRelam extends AuthorizingRealm {
    @Autowired
    private AdminService adminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 访问@RequirePermission注解的url时触发
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Admin adminInfo = (Admin) principals.getPrimaryPrincipal();
        // 获得用户的角色，及权限进行绑定
        for (Role role : adminInfo.getRoleList()) {
            authorizationInfo.addRole(role.getRolename());
            for (Permission p : role.getPermissionList()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    // 验证用户登录信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("验证用户登录信息");
        String username = (String) token.getPrincipal();
        System.out.println("登录用户名： " + username);
        System.out.println(token.getCredentials());

        //从数据库查询出User信息及用户关联的角色，权限信息，以备权限分配时使用
        Admin admin = adminService.findAdminByName(username);
        if (null == admin) return null;

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                admin, // 用户
                admin.getPassword(), // 密码
                getName()  // realm name
        );
        return authenticationInfo;
    }


}
