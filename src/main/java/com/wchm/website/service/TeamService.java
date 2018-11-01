package com.wchm.website.service;

import com.wchm.website.entity.Team;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 团队成员service
 * 所有关于团队成员的增删该查操作都放在此service
 */

@Service
public interface TeamService {

    //接口
    List<Team> queryTeam();

    Result  queryTeamList(Integer pageNum, Integer pageSize);
    //分页
    Result queryTeamByPage(Integer pageNum, Integer pageSize, String team_name);
    //保存
    Result teamSave(Team team);
    //删除
    Result delTeamByID(Integer id);
    //
    ModelAndView teamInfo(Integer id);
    //修改
    Result teamUpdate(Team team);

    Object fomartPartner(HttpServletRequest request);

}
