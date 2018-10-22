package com.wchm.website.service;

import com.wchm.website.entity.Community;
import com.wchm.website.entity.Operation;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    Result queryUserNameAndPwd(String username, String passwrod);

    Result queryUserByToken(String token);

    Result queryIndexData();

    Result loginOut(String token);

    //保存
    Result operationSave(Operation operation);

    Community queryCommunity();
}
