package com.wchm.website.controller;

import com.wchm.website.annotation.UnToken;
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
    @UnToken
    public Object Message(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return messageService.queryMessage();
    }

}
