package com.wchm.website.controller;

import com.wchm.website.entity.Team;
import com.wchm.website.service.TeamService;
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

@Api(tags = "团队")
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("")
    @ResponseBody
    @ApiOperation(value = "团队查询", response = Team.class)
    public Object partner(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return teamService.queryTeam();
    }

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "团队分页查询", response = Team.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "query")
    })
    public Object messageList(HttpServletResponse response, Integer pageNum,
                              Integer pageSize) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //防跨域
        return teamService.queryTeamList(pageNum, pageSize);
    }
}
