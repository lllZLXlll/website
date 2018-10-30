package com.wchm.website.service;

import com.wchm.website.entity.Operation;
import com.wchm.website.entity.Partner;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 操作日志service
 *
 */

@Service
public interface OperationService {

    //分页
    Result queryOperationByPage(Integer pageNum, Integer pageSize, String admin_name);

    //保存
    Long operationSave(Operation operation);

}
