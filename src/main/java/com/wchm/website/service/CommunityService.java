package com.wchm.website.service;

import com.wchm.website.entity.Community;
import com.wchm.website.entity.Team;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 社区数据service，主要用来修改社区关注人数
 * 所有关于社区数据的增删该查操作都放在此service
 */

@Service
public interface CommunityService {

    //接口
    List<Community> queryCommunity();
    //分页
    Result queryCommunityByPage(Integer pageNum, Integer pageSize);
    //保存
    Result communitySave(Community community);
    //删除
    Result delCommunityByID(Integer id);
    //
    ModelAndView communityInfo(Integer id);
    //修改
    Result communityUpdate(Community community);

}
