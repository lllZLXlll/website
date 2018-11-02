package com.wchm.website.controller;

import com.wchm.website.annotation.MyLog;
import com.wchm.website.entity.*;
import com.wchm.website.service.*;
import com.wchm.website.util.ExcelUtils;
import com.wchm.website.util.Result;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    // 首页跳转
    @GetMapping("/index")
    public String index(@CookieValue(value = "token", required = false) String token) {
        return "index";
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
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/news/list")
    public String newsList() {
        return "news-list";
    }

    // 新闻列表数据
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/news/data")
    @ResponseBody
    public Result newsData(Integer pageNum, Integer pageSize, String title, Integer lang) {
        return newsService.queryNewsByPage(pageNum, pageSize, title, lang);
    }

    // 删除新闻
    @MyLog(value = "删除新闻")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/news/del")
    @ResponseBody
    public Result newsDel(Integer id){
        return newsService.delNewsByID(id);
    }

    // 添加新闻跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/news/add")
    public String newsAdd() {
        return "news-add";
    }

    // 添加新闻跳转
    @MyLog("添加新闻")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/news/save")
    @ResponseBody
    public Result newsSave(@RequestBody News news) {
        return newsService.newsSave(news);
    }

    // 查询新闻信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/news/info/{id}")
    public ModelAndView newsInfo(@PathVariable("id") Integer id) {
        return newsService.newsInfo(id);
    }

    // 修改新闻信息
    @MyLog("修改新闻")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"admin", "operates", "finance"}, logical = Logical.OR)
    @GetMapping("/booking/list")
    public String bookingList() {
        return "booking-list";
    }

    // 预售列表数据
    @RequiresRoles(value = {"admin", "operates", "finance"}, logical = Logical.OR)
    @GetMapping("/booking/data")
    @ResponseBody
    public Result bookingData(Integer pageNum, Integer pageSize, String user_name) {
        return bookingService.queryBookingByPage(pageNum, pageSize, user_name);
    }

    // 导出Excel预售表单
    @MyLog("导出预售表单")
    @RequiresRoles(value = {"admin", "operates", "finance"}, logical = Logical.OR)
    @ResponseBody
    @GetMapping("/booking/excel")
    public Object bookingExcel(HttpServletResponse response) throws IOException {
        return bookingService.bookingExport(response);
    }

    /**
     * ------------------关注人数列表--------------
     *
     * @return community
     */
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/community/list")
    public String communityList() {
        return "community-list";
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/community/data")
    @ResponseBody
    public Result communityData(Integer pageNum, Integer pageSize) {
        return communityService.queryCommunityByPage(pageNum, pageSize);
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/community/info/{id}")
    public ModelAndView communityInfo(@PathVariable("id") Integer id) {
        return communityService.communityInfo(id);
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/community/add")
    public String communityAdd() {
        return "community-add";
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/community/save")
    @ResponseBody
    public Result communitySave(@RequestBody Community community) {
        return communityService.communitySave(community);
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @MyLog("关注人数修改")
    @PostMapping("/community/update")
    @ResponseBody
    public Result communityUpdate(@RequestBody Community community) {
        return communityService.communityUpdate(community);
    }

    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @MyLog("关注人数删除")
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
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/notice/list")
    public String noticeList() {
        return "notice-list";
    }

    // 公告列表数据
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/notice/data")
    @ResponseBody
    public Result noticeData(Integer pageNum, Integer pageSize, String title, Integer lang) {
        return noticeService.queryNoticeByPage(pageNum, pageSize, title, lang);
    }

    // 添加公告跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/notice/add")
    public String noticeAdd() {
        return "notice-add";
    }

    // 添加公告跳转
    @MyLog("添加公告")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/notice/save")
    @ResponseBody
    public Result noticeSave(@RequestBody Notice notice) {
        return noticeService.noticeSave(notice);
    }

    // 删除公告
    @MyLog("删除公告")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/notice/del")
    @ResponseBody
    public Result noticeDel(Integer id) {
        return noticeService.delNoticeByID(id);
    }

    // 查询公告信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/notice/info/{id}")
    public ModelAndView noticeInfo(@PathVariable("id") Integer id) {
        return noticeService.noticeInfo(id);
    }

    // 修改公告信息
    @MyLog("修改公告")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/team/list")
    public String teamList() {
        return "team-list";
    }

    // 团队列表数据
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/team/data")
    @ResponseBody
    public Result teamData(Integer pageNum, Integer pageSize, String team_name) {
        return teamService.queryTeamByPage(pageNum, pageSize, team_name);
    }

    // 查询团队信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/team/info/{id}")
    public ModelAndView teamInfo(@PathVariable("id") Integer id) {
        return teamService.teamInfo(id);
    }

    // 添加团队跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/team/add")
    public String teamAdd() {
        return "team-add";
    }

    // 添加团队
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/team/save")
    @ResponseBody
    public Result teamSave(HttpServletRequest request) {
        Team team = (Team) teamService.fomartPartner(request);
        return teamService.teamSave(team);
    }

    // 修改团队信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/team/update")
    @ResponseBody
    public Result teamUpdate(HttpServletRequest request) {
        Team team = (Team) teamService.fomartPartner(request);
        return teamService.teamUpdate(team);
    }


    // 删除团队
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/partner/list")
    public String partnerList() {
        return "partner-list";
    }

    // 合作伙伴列表数据
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/partner/data")
    @ResponseBody
    public Result partnerData(Integer pageNum, Integer pageSize, String partner_name) {
        return partnerService.queryPartnerByPage(pageNum, pageSize, partner_name);
    }

    // 查询合作伙伴信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/partner/info/{id}")
    public ModelAndView partnerInfo(@PathVariable("id") Integer id) {
        return partnerService.partnerInfo(id);
    }

    // 添加合作伙伴跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/partner/add")
    public String partnerAdd() {
        return "partner-add";
    }

    // 添加合作伙伴
    @MyLog("添加合作伙伴")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/partner/save")
    @ResponseBody
    public Result partnerSave(HttpServletRequest request) {
        Partner partner = (Partner) partnerService.fomartPartner(request);
        return partnerService.partnerSave(partner);
    }

    // 修改合作伙伴信息
    @MyLog("修改合作伙伴")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/partner/update")
    @ResponseBody
    public Result partnerUpdate(HttpServletRequest request) {
        Partner partner = (Partner) partnerService.fomartPartner(request);
        return partnerService.partnerUpdate(partner);
    }


    // 删除合作伙伴
    @MyLog("删除合作伙伴")
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
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
    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
    @GetMapping("/currency/list")
    public String currencyList() {
        return "currency-list";
    }

    // 带币池列表数据
    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
    @GetMapping("/currency/data")
    @ResponseBody
    public Result currencyData(Integer pageNum, Integer pageSize, String user_name) {
        return currencyService.queryCurrencyByPage(pageNum, pageSize, user_name);
    }

    /**
     * TODO 第一期的锁仓不需要程序转账，这部分做好的代码保留到第二期可能用得上。
     */

    // 添加带币池跳转
    @MyLog("添加代币池用户")
    @RequiresRoles(value = "admin")
    @PostMapping("/currency/save")
    @ResponseBody
    public Result currencySave(@RequestBody Currency currency) {
        return currencyService.currencySave(currency);
    }

//    // 转账详情列表跳转
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @GetMapping("/currency/record/{id}")
//    public ModelAndView recordInfo(@PathVariable("id") Integer id) {
//        return currencyService.recordInfo(id);
//    }
//
//    // 转账跳转
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @GetMapping("/currency/transfer/{id}")
//    public ModelAndView currencyInfo(@PathVariable("id") Integer id) {
//        return currencyService.currencyInfo(id);
//    }
//
//    // 添加带币池跳转
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @GetMapping("/currency/add")
//    public String currencyAdd() {
//        return "currency-add";
//    }
//
//    // 添加带币池跳转
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @PostMapping("/currency/save")
//    @ResponseBody
//    public Result currencySave(@RequestBody Currency currency) {
//        return currencyService.currencySave(currency);
//    }
//
//    // 删除带币池
//    @PostMapping("/currency/del")
//    @ResponseBody
//    public Result currencyDel(Integer id) {
//        return currencyService.delCurrencyByID(id);
//    }

    // 转账详情列表跳转
    @RequiresRoles(value = "admin")
    @GetMapping("/currency/record/{id}")
    public ModelAndView recordInfo(@PathVariable("id") Integer id) {
        return currencyService.recordInfo(id);
    }

    // 转账跳转
    @RequiresRoles(value = "admin")
    @GetMapping("/currency/transfer/{id}")
    public ModelAndView currencyInfo(@PathVariable("id") Integer id) {
        return currencyService.currencyInfo(id);
    }

    /**
     * 带币池转账
     *
     * @param id    <p>带币池表id</p>
     * @param money <p>需要转账的代币</p>
     * @return
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/currency/transfers/{id}/{money}")
    @ResponseBody
    public Result currencyTransfer(@PathVariable("id") Long id, @PathVariable("money") BigDecimal money) {
        try {
            return currencyService.currencyTransfer(id, money);
        } catch (Exception e) {
            return Result.create().fail(e.getMessage());
        }
    }


//    /**
//     * 带币池转账
//     *
//     * @param id    <p>带币池表id</p>
//     * @param money <p>需要转账的代币</p>
//     * @return
//     */
//    @MyLog("带币池转账")
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @GetMapping("/currency/transfers/{id}/{money}")
//    @ResponseBody
//    public Result currencyTransfer(@PathVariable("id") Long id, @PathVariable("money") BigDecimal money) {
//        try {
//            return currencyService.currencyTransfer(id, money);
//        } catch (Exception e) {
//            return Result.create().fail(e.getMessage());
//        }
//    }
//
//
//    // 修改带币池信息
//    @PostMapping("/currency/update")
//    @ResponseBody
//    public Result currencyUpdate(@RequestBody Currency currency) {
//        return currencyService.currencyUpdate(currency);
//    }
//
//    //带币池用户导入
//    @MyLog("代币池用户导入")
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @PostMapping("/currency/excel")
//    @ResponseBody
//    public Result excelImport(HttpServletRequest request) {
//        try {
//            List<Currency> list = ExcelUtils.readFromExcel(request);
//            if (list == null) {
//                return Result.create().fail("导入失败");
//            }
//            for (Currency currency : list) {
//                currencyService.excelImport(currency);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage() + "-----error-----代币池导入数据失败", e);
//            return Result.create().fail("导入失败，请联系技术，切勿重复操作！");
//        }
//        return Result.create().success("导入成功");
//    }
//
//    // 带币池转账列表数据
//    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
//    @GetMapping("/currency/record/data")
//    @ResponseBody
//    public Result currencyRecordData(Integer pageNum, Integer pageSize, Integer id) {
//        return currencyService.queryCurrencyRecordByPage(pageNum, pageSize, id);
//    }

    /**
     * ------------------用户申请锁仓代币提现列表--------------
     */
    // 带币池列表跳转
    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
    @GetMapping("/applyfor/list")
    public String applyforList() {
        return "applyfor-list";
    }

    // 带币池列表数据
    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
    @GetMapping("/applyfor/data")
    @ResponseBody
    public Result applyforData(Integer pageNum, Integer pageSize) {
        return currencyService.queryApplyforByPage(pageNum, pageSize);
    }

    // 带币池列表数据
    @RequiresRoles(value = {"admin", "finance"}, logical = Logical.OR)
    @PostMapping("/applyfor/submit")
    @ResponseBody
    public Result applyforSubmit(Integer id) {
        return currencyService.applyforSubmit(id);
    }


    /**
     * ------------------消息中心列表--------------
     *
     * @return
     */
    // 消息中心表跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/message/list")
    public String messageList() {
        return "message-list";
    }

    // 消息中心列表数据
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/message/data")
    @ResponseBody
    public Result messageData(Integer pageNum, Integer pageSize, String title) {
        return messageService.queryMessageByPage(pageNum, pageSize, title);
    }

    // 添加消息中心跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/message/add")
    public String messageAdd() {
        return "message-add";
    }

    // 添加消息中心跳转
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/message/save")
    @ResponseBody
    public Result messageSave(@RequestBody Message message) {
        return messageService.messageSave(message);
    }

    // 删除消息中心
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/message/del")
    @ResponseBody
    public Result messageDel(Integer id) {
        return messageService.delMessageByID(id);
    }

    // 查询消息中心信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @GetMapping("/message/info/{id}")
    public ModelAndView messageInfo(@PathVariable("id") Integer id) {
        return messageService.messageInfo(id);
    }

    // 修改消息中心信息
    @RequiresRoles(value = {"admin", "operates"}, logical = Logical.OR)
    @PostMapping("/message/update")
    @ResponseBody
    public Result messageUpdate(@RequestBody Message message) {
        return messageService.messageUpdate(message);
    }

}
