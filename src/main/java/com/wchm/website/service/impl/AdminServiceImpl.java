package com.wchm.website.service.impl;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.mapper.AdminMapper;
import com.wchm.website.mapper.OperationMapper;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import com.wchm.website.util.UUIDUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    OperationMapper operationMapper;

    @Override
    public Result queryUserNameAndPwd(HttpServletRequest request, String username, String password) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            String exception = (String) request.getAttribute("shiroLoginFailure");

            if (exception != null) {
                return Result.create().fail("登录失败");
            }
        } catch (UnknownAccountException e) {
            return Result.create().fail("账号不存在");
        } catch (IncorrectCredentialsException e) {
            return Result.create().fail("密码错误");
        } catch (Exception e) {
            return Result.create().fail("登录失败");
        }

        // 设置session过期时间 30分钟
        SecurityUtils.getSubject().getSession().setTimeout(1000 * 60 * 30);

        Admin admin = adminMapper.findAdminByName(username);
        admin.setPassword(null);

        // 将登录的用户信息放到session中
        request.getSession().setAttribute("admin", admin);

        /** 使用shiro后不再用redis，做登录控制 */
//        //使用JSONObject转换JSON对象
//        JSONObject jsonObject = JSONObject.fromObject(admin);
//        String str = jsonObject.toString();
//
//        // 生成token给前台保存到浏览器头部中
//        Map<String, String> data = new HashMap<>();
//        String token = UUIDUtil.getUUID();
//        data.put("token", token);
//        data.put("admin_name", admin.getUsername());
//
//        // 将admin和角色权限信息的json字符串存入redis里面
//        redisService.setHours(token, str, 3);

        Operation operation = new Operation();
        operation.setAdmin_name(admin.getUsername());
        operation.setOperation_type("登入");
        operation.setCreate_time(new Date());
        operation.setState(1);
        operationMapper.operationSave(operation);
        return Result.create().success("登录成功");
    }

    @Override
    public Result queryUserIsAuthenticated() {
//        if (token.isEmpty()) {
//            return Result.create().fail("00004", "无令牌信息，请重试");
//        }
//
//        String redisStr = redisService.get(token);
//
//        if (StringUtils.isEmpty(redisStr)) {
//            return Result.create().fail("00005", "登录失效，请重新登录");
//        }

        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return Result.create().fail("00005", "登录失效，请重新登录");
        }

        return Result.create().success("登录成功");
    }

    @Override
    public Result queryIndexData() {
        int newsCount = adminMapper.queryNewsCount();
        int noticeCount = adminMapper.queryNoticeCount();
        Map data = new HashMap();
        data.put("newsCount", newsCount);
        data.put("noticeCount", noticeCount);
        return Result.create().success(data);
    }

    /**
     * 退出
     *
     * @return
     */
    @Override
    public Result loginOut() {

        Subject currentUser = SecurityUtils.getSubject();
        Admin admin = (Admin) currentUser.getPrincipals().getPrimaryPrincipal();

        currentUser.logout();


        /** 使用shiro后不再用redis，做登录控制 */
//        String value = redisService.get(token);
//        Admin admin = (Admin) redisService.strToBean(Admin.class, value);

//        //退出时删除Redis数据
//        redisService.remove(token);

        //退出操作插入日志记录
        Operation operation = new Operation();
        operation.setAdmin_name(admin.getUsername());
        operation.setOperation_type("退出");
        operation.setCreate_time(new Date());
        operation.setState(1);
        operationMapper.operationSave(operation);

        return Result.create().success("退出成功");
    }

    @Override
    public Admin findAdminByName(String username) {
        return adminMapper.findAdminByName(username);
    }


}
