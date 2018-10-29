package com.wchm.website.service;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdminService {

    Result queryUserNameAndPwd(HttpServletRequest request, String username, String passwrod);

    Result queryUserByToken(String token);

    Result queryIndexData();

    Result loginOut(String token);

    //保存
    Result operationSave(Operation operation);

    Admin findAdminByName(String username);

}
