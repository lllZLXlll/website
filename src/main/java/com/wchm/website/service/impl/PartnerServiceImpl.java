package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Partner;
import com.wchm.website.mapper.PartnerMapper;
import com.wchm.website.service.PartnerService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
class PartnerServiceImpl implements PartnerService {


    @Autowired
    PartnerMapper partnerMapper;

    /**
     *
     * @return
     */
    @Override
    public List<Partner> queryPartner() {
        return partnerMapper.queryPartner();
    }

    /**
     * 分页
     *
     * @param pageNum      <p>当前页</p>
     * @param pageSize
     * @param partner_name
     * @return
     */
    @Override
    public Result queryPartnerByPage(Integer pageNum, Integer pageSize, String partner_name) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Partner> data;
        if (StringUtil.isEmpty(partner_name)) {
            data = partnerMapper.queryPartnerByPage();
        } else {
            data = partnerMapper.queryPartnerByPageTitle(partner_name);
        }
        PageInfo<Partner> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    //保存
    @Override
    public Result partnerSave(Partner partner) {
        long result = partnerMapper.partnerSave(partner);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }

    //删除
    @Override
    public Result delPartnerByID(Integer id) {
        long result = partnerMapper.delPartnerByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }
        return Result.create().success("删除成功");
    }

    //
    @Override
    public ModelAndView partnerInfo(Integer id) {
        Partner partner = partnerMapper.partnerInfo(id);
        ModelAndView mav = new ModelAndView("partner-edit");
        mav.getModel().put("partner", partner);
        return mav;
    }

    //修改
    @Override
    public Result partnerUpdate(Partner partner) {
        long result = partnerMapper.partnerUpdate(partner);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }


}
