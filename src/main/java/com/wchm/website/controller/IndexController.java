package com.wchm.website.controller;

import com.wchm.website.entity.Booking;
import com.wchm.website.service.BookingService;
import com.wchm.website.service.PaperService;
import com.wchm.website.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Api(tags = "官网首页")
@Controller
@RequestMapping("")
public class IndexController {


    @Autowired
    private BookingService bookingService; //预售

    @Autowired
    private PaperService paperService; //白皮书下载

    @GetMapping("")
    public String index() {
        return "login";
    }

    @GetMapping("/index")
    public String indexs() {
        return "login";
    }

    /**
     * 保存预售
     *
     * @param
     * @return
     */

    @ApiOperation(value = "预售表单信息提交")
    @PostMapping("/booking/save")
    @ResponseBody
    public Result bookingSave(HttpServletResponse response, @RequestBody Booking booking) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        booking.setCreate_time(new Date());
        booking.setState(1);
        return bookingService.bookingSave(booking);
    }

//    /**
//     * 白皮书下载
//     *
//     * @param response
//     * @param request
//     * @param number
//     * @return
//     * @throws IOException
//     */
//    @GetMapping("/paper")
//    @ApiOperation(value = "白皮书下载")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "number", value = "1中文版，2英文版", required = true)
//    })
//    public Result paperDate(HttpServletResponse response, HttpServletRequest request, String number) throws IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");//防止跨域
//        return paperService.paperSave(number, response, request);
//    }


//    @GetMapping("/socket")
//    public String webSocket(HttpServletResponse response) {
//        return "websocket";
//    }


}
