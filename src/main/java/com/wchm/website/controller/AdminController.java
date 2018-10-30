package com.wchm.website.controller;

import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.*;
import com.wchm.website.service.*;
import com.wchm.website.util.DateUtil;
import com.wchm.website.util.ExcelUtils;
import com.wchm.website.util.Result;
import com.wchm.website.util.UploadUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "后台")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${wchm.update-image-relative}")
    private String relative;

    @Value("${wchm.update-image-absolutely}")
    private String absolutely;

    @Autowired
    private RedisService redisService;

    public final static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;//登录

    @Autowired
    private NewsService newsService; //新闻

    @Autowired
    private NoticeService noticeService;//标题

    @Autowired
    private TeamService teamService;//团队

    @Autowired
    private PartnerService partnerService;//合作伙伴

    @Autowired
    private CurrencyService currencyService;//代币池

    @Autowired
    private CommunityService communityService;//社区关注人数

    @Autowired
    private OperationService operationService; //操作日志

    @Autowired
    private BookingService bookingService; //预售

    @Autowired
    private MessageService messageService; //消息中心


    @GetMapping("")
    public String admin() {
        return "login";
    }

    // 登录
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 登录
    @PostMapping("/login/to")
    @ResponseBody
    public Result loginTo(HttpServletRequest request, String username, String password) {
        // 账号或者密码为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.create().fail("00001", "账号或密码为空");
        }

        Result result = adminService.queryUserNameAndPwd(request, username, password);

        return result;
    }

    // 退出
    @GetMapping("/login/out")
    @ResponseBody
    public Result loginOut() {
        return adminService.loginOut();
    }

    // 首页跳转初始化
    @PostMapping("/index/init")
    @ResponseBody
    public Result indexInit() {
        return adminService.queryUserIsAuthenticated();
    }

    // 首页跳转初始化
    @PostMapping("/index/data")
    @ResponseBody
    public Result indexData() {
        return adminService.queryIndexData();
    }

    // 首页跳转
    @GetMapping("/index")
    public String index(@CookieValue(value = "token", required = false) String token) {
        return "index";
    }

    // 欢迎页跳转
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }


    /**
     * ------------------新闻--------------
     *
     * @return
     */
    // 新闻列表跳转
    @GetMapping("/news/list")
    public String newsList() {
        return "news-list";
    }

    // 新闻列表数据
    @GetMapping("/news/data")
    @ResponseBody
    public Result newsData(Integer pageNum, Integer pageSize, String title, Integer lang) {
        return newsService.queryNewsByPage(pageNum, pageSize, title, lang);
    }

    // 删除新闻
    @PostMapping("/news/del")
    @ResponseBody
    public Result newsDel(Integer id) {
        return newsService.delNewsByID(id);
    }

    // 添加新闻跳转
    @GetMapping("/news/add")
    public String newsAdd() {
        return "news-add";
    }

    // 添加新闻跳转
    @PostMapping("/news/save")
    @ResponseBody
    public Result newsSave(@RequestBody News news) {
        return newsService.newsSave(news);
    }

    // 查询新闻信息
    @GetMapping("/news/info/{id}")
    public ModelAndView newsInfo(@PathVariable("id") Integer id) {
        return newsService.newsInfo(id);
    }

    // 修改新闻信息
    @PostMapping("/news/update")
    @ResponseBody
    public Result newsUpdate(@RequestBody News news) {
        return newsService.newsUpdate(news);
    }


    /**
     * ----------------预售列表数据----------------
     *
     * @return
     */

    // 预售列表跳转
    @GetMapping("/booking/list")
    public String bookingList() {
        return "booking-list";
    }

    // 预售列表数据
    @GetMapping("/booking/data")
    @ResponseBody
    public Result bookingData(Integer pageNum, Integer pageSize, String user_name) {
        return bookingService.queryBookingByPage(pageNum, pageSize, user_name);
    }


    /**
     * ------------------关注人数列表--------------
     *
     * @return community
     */

    @GetMapping("/community/list")
    public String communityList() {
        return "community-list";
    }


    @GetMapping("/community/data")
    @ResponseBody
    public Result communityData(Integer pageNum, Integer pageSize) {
        return communityService.queryCommunityByPage(pageNum, pageSize);
    }


    @GetMapping("/community/info/{id}")
    public ModelAndView communityInfo(@PathVariable("id") Integer id) {
        return communityService.communityInfo(id);
    }


    @GetMapping("/community/add")
    public String communityAdd() {
        return "community-add";
    }


    @PostMapping("/community/save")
    @ResponseBody
    public Result communitySave(@RequestBody Community community) {
        return communityService.communitySave(community);
    }


    @PostMapping("/community/update")
    @ResponseBody
    public Result communityUpdate(@RequestBody Community community) {
        return communityService.communityUpdate(community);
    }


    @PostMapping("/community/del")
    @ResponseBody
    public Result communityDel(Integer id) {
        return communityService.delCommunityByID(id);
    }

    /**
     * ------------------公告列表--------------
     *
     * @return
     */
    // 公告列表跳转
    @GetMapping("/notice/list")
    public String noticeList() {
        return "notice-list";
    }

    // 公告列表数据
    @GetMapping("/notice/data")
    @ResponseBody
    public Result noticeData(Integer pageNum, Integer pageSize, String title, Integer lang) {
        return noticeService.queryNoticeByPage(pageNum, pageSize, title, lang);
    }

    // 添加公告跳转
    @GetMapping("/notice/add")
    public String noticeAdd() {
        return "notice-add";
    }

    // 添加公告跳转
    @PostMapping("/notice/save")
    @ResponseBody
    public Result noticeSave(@RequestBody Notice notice) {
        return noticeService.noticeSave(notice);
    }

    // 删除公告
    @PostMapping("/notice/del")
    @ResponseBody
    public Result noticeDel(Integer id) {
        return noticeService.delNoticeByID(id);
    }

    // 查询公告信息
    @GetMapping("/notice/info/{id}")
    public ModelAndView noticeInfo(@PathVariable("id") Integer id) {
        return noticeService.noticeInfo(id);
    }

    // 修改公告信息
    @PostMapping("/notice/update")
    @ResponseBody
    public Result newsUpdate(@RequestBody Notice notice) {
        return noticeService.newsUpdate(notice);
    }


    /**
     * ------------------团队数据--------------
     *
     * @return
     */
    // 团队列表跳转
    @GetMapping("/team/list")
    public String teamList() {
        return "team-list";
    }

    // 团队列表数据
    @GetMapping("/team/data")
    @ResponseBody
    public Result teamData(Integer pageNum, Integer pageSize, String team_name) {
        return teamService.queryTeamByPage(pageNum, pageSize, team_name);
    }

    // 查询团队信息
    @GetMapping("/team/info/{id}")
    public ModelAndView teamInfo(@PathVariable("id") Integer id) {
        return teamService.teamInfo(id);
    }

    // 添加团队跳转
    @GetMapping("/team/add")
    public String teamAdd() {
        return "team-add";
    }

    // 添加团队
    @PostMapping("/team/save")
    @ResponseBody
    public Result teamSave(HttpServletRequest request) {
        Team team = fomartTeam(request);
        return teamService.teamSave(team);
    }

    // 修改团队信息
    @PostMapping("/team/update")
    @ResponseBody
    public Result teamUpdate(HttpServletRequest request) {
        Team team = fomartTeam(request);
        return teamService.teamUpdate(team);
    }

    /**
     * 上传图片，并把表单数据封装到对象中
     * 团队头像管理
     *
     * @param request
     * @return
     */
    private Team fomartTeam(HttpServletRequest request) {
        Team team = new Team();
        try {
            String imgPath = UploadUtil.imageUpload(request, relative, absolutely);
            team.setHead(imgPath);
        } catch (Exception e) {
            log.error("上传图片异常");
            e.printStackTrace();
        }
        String idStr = request.getParameter("id");
        Long id = null;
        if (idStr != null) {
            id = Long.parseLong(idStr);
        }

        team.setId(id);
        team.setNumber(request.getParameter("number"));
        team.setTeam_name(request.getParameter("team_name"));
        team.setDescription(request.getParameter("description"));
        team.setCreate_time(DateUtil.parseDefaultDate(request.getParameter("create_time")));
        team.setState(Integer.parseInt(request.getParameter("state")));
        return team;
    }

    // 删除团队
    @PostMapping("/team/del")
    @ResponseBody
    public Result teamDel(Integer id) {
        return teamService.delTeamByID(id);
    }


    /**
     * ------------------合作伙伴--------------
     *
     * @return
     */
    // 合作伙伴列表跳转
    @GetMapping("/partner/list")
    public String partnerList() {
        return "partner-list";
    }

    // 合作伙伴列表数据
    @GetMapping("/partner/data")
    @ResponseBody
    public Result partnerData(Integer pageNum, Integer pageSize, String partner_name) {
        return partnerService.queryPartnerByPage(pageNum, pageSize, partner_name);
    }

    // 查询合作伙伴信息
    @GetMapping("/partner/info/{id}")
    public ModelAndView partnerInfo(@PathVariable("id") Integer id) {
        return partnerService.partnerInfo(id);
    }

    // 添加合作伙伴跳转
    @GetMapping("/partner/add")
    public String partnerAdd() {
        return "partner-add";
    }

    // 添加合作伙伴
    @PostMapping("/partner/save")
    @ResponseBody
    public Result partnerSave(HttpServletRequest request) {
        Partner partner = fomartPartner(request);
        return partnerService.partnerSave(partner);
    }

    // 修改合作伙伴信息
    @PostMapping("/partner/update")
    @ResponseBody
    public Result partnerUpdate(HttpServletRequest request) {
        Partner partner = fomartPartner(request);
        return partnerService.partnerUpdate(partner);
    }

    /**
     * 上传图片，并把表单数据封装到对象中
     * 合作伙伴图片管理
     *
     * @param request
     * @return
     */
    private Partner fomartPartner(HttpServletRequest request) {
        Partner partner = new Partner();
        try {
            String imgPath = UploadUtil.imageUpload(request, relative, absolutely);
            partner.setPicture(imgPath);
        } catch (Exception e) {
            log.error("上传图片异常");
            e.printStackTrace();
        }
        String idStr = request.getParameter("id");
        Long id = null;
        if (idStr != null) {
            id = Long.parseLong(idStr);
        }

        partner.setId(id);
        partner.setNumber(request.getParameter("number"));
        partner.setPartner_name(request.getParameter("partner_name"));
        partner.setLink(request.getParameter("link"));
        partner.setCreate_time(DateUtil.parseDefaultDate(request.getParameter("create_time")));
        partner.setState(Integer.parseInt(request.getParameter("state")));
        return partner;
    }

    // 删除合作伙伴
    @PostMapping("/partner/del")
    @ResponseBody
    public Result partnerDel(Integer id) {
        return partnerService.delPartnerByID(id);
    }


    /**
     * ------------------操作日志列表--------------
     *
     * @return
     */
    // 操作日志列表跳转
    @RequiresRoles(value = "admin") // 需要管理员权限
    @GetMapping("/operation/list")
    public String operationList() {
        return "operation-list";
    }

    // 操作日志列表数据
    @RequiresRoles(value = "admin")
    @GetMapping("/operation/data")
    @ResponseBody
    public Result operationData(Integer pageNum, Integer pageSize, String admin_name) {
        return operationService.queryOperationByPage(pageNum, pageSize, admin_name);
    }

    /**
     * ------------------代币池列表--------------
     *
     * @return
     */
    // 带币池列表跳转
    @GetMapping("/currency/list")
    public String currencyList() {
        return "currency-list";
    }

    // 带币池列表数据
    @GetMapping("/currency/data")
    @ResponseBody
    public Result currencyData(Integer pageNum, Integer pageSize, String user_name) {
        return currencyService.queryCurrencyByPage(pageNum, pageSize, user_name);
    }

    // 添加带币池跳转
    @GetMapping("/currency/add")
    public String currencyAdd() {
        return "currency-add";
    }

    // 添加带币池跳转
    @PostMapping("/currency/save")
    @ResponseBody
    public Result currencySave(@RequestBody Currency currency) {
        return currencyService.currencySave(currency);
    }

//    // 删除带币池
//    @PostMapping("/currency/del")
//    @ResponseBody
//    public Result currencyDel(Integer id) {
//        return currencyService.delCurrencyByID(id);
//    }

    // 转账详情列表跳转
    @GetMapping("/currency/record/{id}")
    public ModelAndView recordInfo(@PathVariable("id") Integer id) {
        return currencyService.recordInfo(id);
    }

    // 转账跳转
    @GetMapping("/currency/transfer/{id}")
    public ModelAndView currencyInfo(@PathVariable("id") Integer id) {
        return currencyService.currencyInfo(id);
    }

    /**
     * ------------------消息中心列表--------------
     *
     * @return
     */
    // 消息中心表跳转
    @GetMapping("/message/list")
    public String messageList() {
        return "message-list";
    }

    // 消息中心列表数据
    @GetMapping("/message/data")
    @ResponseBody
    public Result messageData(Integer pageNum, Integer pageSize, String title) {
        return messageService.queryMessageByPage(pageNum, pageSize, title);
    }

    // 添加消息中心跳转
    @GetMapping("/message/add")
    public String messageAdd() {
        return "message-add";
    }

    // 添加消息中心跳转
    @PostMapping("/message/save")
    @ResponseBody
    public Result messageSave(@RequestBody Message message) {
        return messageService.messageSave(message);
    }

    // 删除消息中心
    @PostMapping("/message/del")
    @ResponseBody
    public Result messageDel(Integer id) {
        return messageService.delMessageByID(id);
    }

    // 查询消息中心信息
    @GetMapping("/message/info/{id}")
    public ModelAndView messageInfo(@PathVariable("id") Integer id) {
        return messageService.messageInfo(id);
    }

    // 修改消息中心信息
    @PostMapping("/message/update")
    @ResponseBody
    public Result messageUpdate(@RequestBody Message message) {
        return messageService.messageUpdate(message);
    }

    /**
     * 带币池转账
     *
     * @param id    <p>带币池表id</p>
     * @param money <p>需要转账的代币</p>
     * @return
     */
    @GetMapping("/currency/transfers/{id}/{money}")
    @ResponseBody
    public Result currencyTransfer(@PathVariable("id") Long id, @PathVariable("money") BigDecimal money) {
        try {
            return currencyService.currencyTransfer(id, money);
        } catch (Exception e) {
            return Result.create().fail(e.getMessage());
        }
    }


//    // 修改带币池信息
//    @PostMapping("/currency/update")
//    @ResponseBody
//    public Result currencyUpdate(@RequestBody Currency currency) {
//        return currencyService.currencyUpdate(currency);
//    }

    //带币池用户导入
    @PostMapping("/currency/excel")
    @ResponseBody
    public Result excelImport(HttpServletRequest request) {
        try {
            List<Currency> list = ExcelUtils.readFromExcel(request);
            if (list == null) {
                return Result.create().fail("导入失败");
            }
            for (Currency currency : list) {
                currencyService.excelImport(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.create().fail("导入失败，请联系技术，切勿重复操作！");
        }
        return Result.create().success("导入成功");
    }

    // 带币池转账列表数据
    @GetMapping("/currency/record/data")
    @ResponseBody
    public Result currencyRecordData(Integer pageNum, Integer pageSize, Integer id) {
        return currencyService.queryCurrencyRecordByPage(pageNum, pageSize, id);
    }


    //访问此连接时会触发MyShiroRealm中的权限分配方法
    @GetMapping("/test")
//    @RequiresPermissions({"index:update", "index:add"}) // 访上问此接口需要的权限
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR) // 访问此接口需要的角色，AND:且，OR:或
    @ResponseBody
    public String test2() {
        System.out.println("permission  test");
        return "test";
    }

    // 没有权限页面
    @GetMapping("/403")
    public String to403() {
        return "403";
    }

}
