package com.wchm.website.service;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdminService {

    Result queryUserNameAndPwd(HttpServletRequest request, String username, String passwrod);

    Result queryUserIsAuthenticated();

    Result queryIndexData();

    Result loginOut();

    Admin findAdminByName(String username);

}
