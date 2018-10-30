package com.wchm.website.controller;

import com.wchm.website.entity.Message;
import com.wchm.website.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "消息")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "消息中心首页", response = Message.class)
    public Object Message(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return messageService.queryMessage();
    }

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "消息分页查询", response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "query")
    })
    public Object messageList(HttpServletResponse response, Integer pageNum,
                              Integer pageSize) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return messageService.queryMessageList(pageNum, pageSize);
    }

    /*@GetMapping("/info")
    @ResponseBody
    @ApiOperation(value = "消息", response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true,paramType = "query"),
    })
    public Object messageInfo(HttpServletResponse response,Long id) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return messageService.messageInfoo(id);
    }*/

}
