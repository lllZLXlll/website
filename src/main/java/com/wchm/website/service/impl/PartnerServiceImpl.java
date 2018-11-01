package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.controller.AdminController;
import com.wchm.website.entity.Partner;
import com.wchm.website.mapper.PartnerMapper;
import com.wchm.website.service.PartnerService;
import com.wchm.website.util.DateUtil;
import com.wchm.website.util.Result;
import com.wchm.website.util.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
class PartnerServiceImpl implements PartnerService {


    @Autowired
    PartnerMapper partnerMapper;

    @Value("${wchm.update-image-relative}")
    private String relative;

    @Value("${wchm.update-image-absolutely}")
    private String absolutely;

    public final static Logger log = LoggerFactory.getLogger(PartnerService.class);

    /**
     *
     * @return
     */
    @Override
    public List<Partner> queryPartner() {
        return partnerMapper.queryPartner();
    }

    @Override
    public Result queryPartnerList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Partner> data1;
        data1 = partnerMapper.queryPartnerByPage();
        PageInfo<Partner> p = new PageInfo(data1);
        return Result.create().success("查询成功", p);
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
        if (StringUtil.isEmpty(partner.getNumber())){
            return Result.create().fail("序号不能为空");
        }
        if (StringUtil.isEmpty(partner.getPartner_name())){
            return Result.create().fail("合作伙伴名称不能为空");
        }
        if (StringUtil.isEmpty(partner.getLink())){
            return Result.create().fail("链接不能为空");
        }
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
        if (StringUtil.isEmpty(partner.getNumber())){
            return Result.create().fail("序号不能为空");
        }
        if (StringUtil.isEmpty(partner.getPartner_name())){
            return Result.create().fail("合作伙伴名称不能为空");
        }
        if (StringUtil.isEmpty(partner.getLink())){
            return Result.create().fail("链接不能为空");
        }
        long result = partnerMapper.partnerUpdate(partner);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }
    /**
     * 上传图片，并把表单数据封装到对象中
     * 合作伙伴图片管理
     *
     * @param request
     * @return
     */
    public Partner fomartPartner(HttpServletRequest request) {
        Partner partner = new Partner();
        try {
            String imgPath = UploadUtil.imageUpload(request, relative, absolutely);
            partner.setPicture(imgPath);
        } catch (Exception e) {
            log.error("上传图片异常", e);
            e.printStackTrace();
        }
        String idStr = request.getParameter("id");
        Long id = null;
        if (idStr != null) {
            id = Long.parseLong(idStr);
        }

        partner.setId(id);
        partner.setNumber(request.getParameter("number"));
        partner.setPartner_name(request.getParameter("partner_name"));
        partner.setLink(request.getParameter("link"));
        partner.setCreate_time(DateUtil.parseDefaultDate(request.getParameter("create_time")));
        partner.setState(Integer.parseInt(request.getParameter("state")));
        return partner;
    }

}
