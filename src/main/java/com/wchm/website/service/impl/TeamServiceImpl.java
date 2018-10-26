package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Team;
import com.wchm.website.mapper.TeamMapper;
import com.wchm.website.service.TeamService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*
 新闻Service
 */
@Service
class TeamServiceImpl implements TeamService {

    @Autowired
    TeamMapper teamMapper;


    @Override
    public List<Team> queryTeam() {
        return teamMapper.queryTeam();
    }

    @Override
    public Result queryTeamList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Team> data;
        data = teamMapper.queryTeamByPage();
        PageInfo<Team> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    //团队分页
    @Override
    public Result queryTeamByPage(Integer pageNum, Integer pageSize, String team_name) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Team> data;
        if (StringUtil.isEmpty(team_name)) {
            data = teamMapper.queryTeamByPage();
        } else {
            data = teamMapper.queryTeamByPageTitle(team_name);
        }
        PageInfo<Team> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    //删除团队
    @Override
    public Result delTeamByID(Integer id) {
        long result = teamMapper.delTeamByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }
        return Result.create().success("删除成功");
    }

    @Override
    public ModelAndView teamInfo(Integer id) {
        Team team = teamMapper.teamInfo(id);
        ModelAndView mav = new ModelAndView("team-edit");
        mav.getModel().put("team", team);
        return mav;
    }

    //修改团队
    @Override
    public Result teamUpdate(Team team) {
        if (StringUtil.isEmpty(team.getNumber())){
            return Result.create().fail("序号不能为空！");
        }
        if (StringUtil.isEmpty(team.getDescription())){
            return Result.create().fail("描述不能为空！");
        }
        if (StringUtil.isEmpty(team.getNumber())){
            return Result.create().fail("序号不能为空！");
        }
        long result = teamMapper.teamUpdate(team);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }


    //保存团队
    @Override
    public Result teamSave(Team team) {
        if (StringUtil.isEmpty(team.getNumber())){
            return Result.create().fail("序号不能为空！");
        }
        if (StringUtil.isEmpty(team.getDescription())){
            return Result.create().fail("描述不能为空！");
        }
        if (StringUtil.isEmpty(team.getNumber())){
            return Result.create().fail("序号不能为空！");
        }
   /*     if (StringUtil.isEmpty(team.getCreate_time())){
            return Result.create().fail("时间不能为空！");
        }*/
        long result = teamMapper.teamSave(team);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }


/*    //插入团队
    @Override
    public ModelAndView teamInfo(Integer id) {
        Team team = teamMapper.teamInfo(id);
        ModelAndView mav = new ModelAndView("team-edit");
        mav.getModel().put("team", team);
        return mav;
    }*/


}