package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.News;
import com.wchm.website.entity.Notice;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.NewsService;
import com.wchm.website.service.NoticeService;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Api(tags = "后台")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RedisService redisService;


    public final static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NoticeService noticeService;

    @GetMapping("")
    @UnToken
    public String admin() {
        return "login";
    }

    // 登录
    @GetMapping("/login")
    @UnToken
    public String login() {
        return "login";
    }

    // 登录
    @PostMapping("/login/to")
    @ResponseBody
    @UnToken
    public Result loginTo(@Param("username") String username, @Param("password") String password) {
        // 账号或者密码为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.create().fail("00001", "账号或密码为空");
        }

        // 查询数据库是否有此用户
        Result result = adminService.queryUserNameAndPwd(username, password);

        return result;
    }

    // 首页跳转初始化
    @PostMapping("/index/init")
    @ResponseBody
    public Result indexInit(@CookieValue("token") String token) {
        return adminService.queryUserByToken(token);
    }

    // 首页跳转初始化
    @PostMapping("/index/data")
    @ResponseBody
    public Result indexData(@CookieValue("token") String token) {
        return adminService.queryIndexData();
    }

    // 首页跳转
    @GetMapping("/index")
    public String index(@CookieValue(value="token",required=false) String token) {
        return "index";
    }

    // 欢迎页跳转
    @GetMapping("/welcome")
    public String welcome(@CookieValue("token") String token) {
        return "welcome";
    }

    // 新闻列表跳转
    @GetMapping("/news/list")
    public String newsList(@CookieValue("token") String token) {
        return "news-list";
    }

    // 新闻列表数据
    @GetMapping("/news/data")
    @ResponseBody
    public Result newsData(@CookieValue("token") String token, Integer pageNum, Integer pageSize, String title) {
        return newsService.queryNewsByPage(pageNum, pageSize, title);
    }

    // 删除新闻
    @PostMapping("/news/del")
    @ResponseBody
    public Result newsDel(@CookieValue("token") String token, Integer id) {
        return newsService.delNewsByID(id);
    }

    // 添加新闻跳转
    @GetMapping("/news/add")
    public String newsAdd(@CookieValue("token") String token) {
        return "news-add";
    }

    // 添加新闻跳转
    @PostMapping("/news/save")
    @ResponseBody
    public Result newsSave(@CookieValue("token") String token, @RequestBody News news) {
        return newsService.newsSave(news);
    }

    // 查询新闻信息
    @GetMapping("/news/info/{id}")
    public ModelAndView newsInfo(@CookieValue("token") String token, @PathVariable("id") Integer id) {
        return newsService.newsInfo(id);
    }

    // 修改新闻信息
    @PostMapping("/news/update")
    @ResponseBody
    public Result newsUpdate(@CookieValue("token") String token, @RequestBody News news) {
        return newsService.newsUpdate(news);
    }

    // 公告列表跳转
    @GetMapping("/notice/list")
    public String noticeList(@CookieValue("token") String token) {
        return "notice-list";
    }

    // 公告列表数据
    @GetMapping("/notice/data")
    @ResponseBody
    public Result noticeData(@CookieValue("token") String token, Integer pageNum, Integer pageSize, String title) {
        return noticeService.queryNoticeByPage(pageNum, pageSize, title);
    }

    // 添加公告跳转
    @GetMapping("/notice/add")
    public String noticeAdd(@CookieValue("token") String token) {
        return "notice-add";
    }

    // 添加公告跳转
    @PostMapping("/notice/save")
    @ResponseBody
    public Result noticeSave(@CookieValue("token") String token, @RequestBody Notice notice) {
        return noticeService.noticeSave(notice);
    }

    // 删除公告
    @PostMapping("/notice/del")
    @ResponseBody
    public Result noticeDel(@CookieValue("token") String token, Integer id) {
        return noticeService.delNoticeByID(id);
    }

    // 查询公告信息
    @GetMapping("/notice/info/{id}")
    public ModelAndView noticeInfo(@CookieValue("token") String token, @PathVariable("id") Integer id) {
        return noticeService.noticeInfo(id);
    }

    // 修改公告信息
    @PostMapping("/notice/update")
    @ResponseBody
    public Result newsUpdate(@CookieValue("token") String token, @RequestBody Notice notice) {
        return noticeService.newsUpdate(notice);
    }

    // 退出
    @GetMapping("/login/out")
    @ResponseBody
    public Result loginOut(@CookieValue("token") String token) {
        return adminService.loginOut(token);
    }


}
