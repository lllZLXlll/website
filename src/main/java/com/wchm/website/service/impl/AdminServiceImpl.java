package com.wchm.website.service.impl;

import com.wchm.website.entity.Admin;
import com.wchm.website.mapper.AdminMapper;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import com.wchm.website.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RedisService redisService;

    //    @Transactional
    @Override
    public Result queryUserNameAndPwd(String username, String password) {
        Admin admin = adminMapper.queryUserNameAndPwd(username);

        // 账号错
        if (admin == null) {
            return Result.create().fail("00002", "账号错误");
        }

        // 密码错
        if (!admin.getPassword().equals(password)) {
            return Result.create().fail("00003", "密码错误");
        }

        // 生成token给前台保存到浏览器头部中
        Map<String, String> data = new HashMap<>();
        String token = UUIDUtil.getUUID();
        data.put("token", token);
        data.put("admin_name", admin.getUsername());

        // 暂时没有用redis，只能先将token保存在数据库中
        /* adminMapper.updateAdminToken(admin.getId(), token);*/

        //将token存入redis里面
        redisService.setHours(token, admin.getUsername(), 3);


        return Result.create().success("登录成功", data);
    }

    @Override
    public Result queryUserByToken(String token) {
        if (token.isEmpty()) {
            return Result.create().fail("00004", "无令牌信息，请重试");
        }

        String redisStr = redisService.get(token);

        if (StringUtils.isEmpty(redisStr)) {
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

    @Override
    public Result loginOut(String token) {
        redisService.remove(token);
        return Result.create().success("退出成功");
    }

}
