package com.wchm.website.service.impl;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Community;
import com.wchm.website.entity.Operation;
import com.wchm.website.entity.Partner;
import com.wchm.website.mapper.AdminMapper;
import com.wchm.website.mapper.CommunityMapper;
import com.wchm.website.mapper.OperationMapper;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import com.wchm.website.util.UUIDUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.management.ValueExp;
import java.util.*;

@Service
class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    OperationMapper operationMapper;
    //    @Transactional
    @Override
    public Result queryUserNameAndPwd(String username, String password) {
        Admin admin = adminMapper.queryUserNameAndPwd(username);

        //使用JSONObject转换JSON对象
        JSONObject jsonObject = JSONObject.fromObject(admin);
        String str = jsonObject.toString();

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

        //把将token和username存入redis里面，json转换成对象存入redis里面
        redisService.setHours(token, str, 3);

        Operation operation =new Operation();
        operation.setAdmin_name(admin.getUsername());
        operation.setOperation_type("1");
        operation.setCreate_time(new Date());
        operation.setState(1);
        operationMapper.operationSave(operation);
        return  Result.create().success("登录成功", data);


    }

    /**
     * 将日志插入数据库
     * @param operation
     * @return
     */
    @Override
    public Result operationSave(Operation operation) {
        operationMapper.operationSave(operation);
        return Result.create().success("添加成功");
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

    /**
     * 退出
     * @param token
     * @return
     */
    @Override
    public Result loginOut(String token) {


        String value = redisService.get(token);
        Admin admin = (Admin) redisService.strToBean(Admin.class, value);

        //退出时删除Redis数据
        redisService.remove(token);

        //退出操作插入日志记录
        Operation operation =new Operation();
        operation.setAdmin_name(admin.getUsername());
        operation.setOperation_type("2");
        operation.setCreate_time(new Date());
        operation.setState(1);
        operationMapper.operationSave(operation);

        return Result.create().success("退出成功");
    }


    @Override
    public  List<Community> queryCommunity() {
        return communityMapper.queryCommunity();
    }

}
