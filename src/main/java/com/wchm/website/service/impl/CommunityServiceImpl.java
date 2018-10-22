package com.wchm.website.service.impl;

import com.wchm.website.entity.Community;
import com.wchm.website.entity.Notice;
import com.wchm.website.mapper.CommunityMapper;
import com.wchm.website.service.CommunityService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service
class CommunityServiceImpl implements CommunityService {


    @Autowired
    CommunityMapper communityMapper;

    @Override
    public Result communityData() {
        Community community = communityMapper.queryCommunity();
        List<Community> list = new ArrayList<>();
        list.add(community);
        return Result.create().success("查询成功", list);
    }

    @Override
    public ModelAndView communityInfo(Integer id) {
        Community community = communityMapper.communityInfo(id);
        ModelAndView mav = new ModelAndView("community-edit");
        mav.getModel().put("community", community);
        return mav;
    }

    @Override
    public Result communityUpdate(Community community) {
        long result = communityMapper.communityUpdate(community);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }
}
