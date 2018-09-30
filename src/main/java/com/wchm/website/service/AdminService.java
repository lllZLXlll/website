package com.wchm.website.service;

import com.wchm.website.entity.Admin;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public interface AdminService {

    Result queryUserNameAndPwd(String username, String passwrod);

    Result queryUserByToken(String token);

    Result queryIndexData();

    Result loginOut(String token);
}
