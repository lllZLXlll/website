package com.wchm.website.service;

import com.wchm.website.entity.Partner;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 合作伙伴service
 * 所有关于合作伙伴的增删该查操作都放在此service
 */

@Service
public interface PartnerService {

    // 官网前台查询所有团队信息
    List<Partner> queryPartner();

    // 分页
    Result queryPartnerByPage(Integer pageNum, Integer pageSize, String partner_name);

    Result  queryPartnerList(Integer pageNum, Integer pageSize);

    // 保存
    Result partnerSave(Partner partner);

    // 删除
    Result delPartnerByID(Integer id);

    // 编辑跳转
    ModelAndView partnerInfo(Integer id);

    // 修改
    Result partnerUpdate(Partner partner);

    Object fomartPartner(HttpServletRequest request);
}
