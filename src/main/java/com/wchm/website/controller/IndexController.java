package com.wchm.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class IndexController {

    @GetMapping("")
    public String index() {
        return "login";
    }

    @GetMapping("/index")
    public String indexs() {
        return "login";
    }


}
