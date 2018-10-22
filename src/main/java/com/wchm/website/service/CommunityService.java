package com.wchm.website.service;

import com.wchm.website.entity.Community;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

/**
 * 社区数据service，主要用来修改社区关注人数
 * 所有关于社区数据的增删该查操作都放在此service
 */

@Service
public interface CommunityService {

    Result communityData();

    ModelAndView communityInfo(Integer id);

    Result communityUpdate(Community community);
}
