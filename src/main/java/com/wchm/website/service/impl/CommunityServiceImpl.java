package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Community;
import com.wchm.website.mapper.CommunityMapper;
import com.wchm.website.service.CommunityService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
class CommunityServiceImpl implements CommunityService {


    @Autowired
    CommunityMapper communityMapper;

    @Override
    public List<Community> queryCommunity() {
        return  communityMapper.queryCommunity();
    }

    /**
     * 官网关注列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result queryCommunityByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Community> data;
        data = communityMapper.queryCommunityByPage();
        PageInfo<Community> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }



    /**
     * 保存
     * @param community
     * @return
     */
    @Override
    public Result communitySave(Community community) {
        if(StringUtil.isEmpty(community.getEnglish_name())){
            return Result.create().fail("英文名不能为空");
        }
        if(StringUtil.isEmpty(community.getDescription())){
            return Result.create().fail("中文描述不能为空");
        }
        if(StringUtil.isEmpty(community.getLink())){
            return Result.create().fail("链接不能为空");
        }
        if(community.getFollow_number()==null){
            return Result.create().fail("关注数量不能为空");
        }

        long result = communityMapper.communitySave(community);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public Result delCommunityByID(Integer id) {
        long result = communityMapper.delCommunityByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }
        return Result.create().success("删除成功");
    }

    /**
     *修改时获取ID
     * @param id
     * @return
     */
    @Override
    public ModelAndView communityInfo(Integer id) {
        Community community = communityMapper.communityInfo(id);
        ModelAndView mav = new ModelAndView("community-edit");
        mav.getModel().put("community", community);
        return mav;
    }

    /**
     * 修改
     * @param community
     * @return
     */
    @Override
    public Result communityUpdate(Community community) {
        if(StringUtil.isEmpty(community.getEnglish_name())){
            return Result.create().fail("英文名不能为空");
        }
        if(StringUtil.isEmpty(community.getDescription())){
            return Result.create().fail("中文描述不能为空");
        }
        if(StringUtil.isEmpty(community.getLink())){
            return Result.create().fail("链接不能为空");
        }
        if(community.getFollow_number()==null){
            return Result.create().fail("关注数量不能为空");
        }
        long result = communityMapper.communityUpdate(community);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");

    }
}
