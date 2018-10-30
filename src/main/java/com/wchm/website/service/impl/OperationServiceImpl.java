package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Operation;
import com.wchm.website.entity.Partner;
import com.wchm.website.mapper.OperationMapper;
import com.wchm.website.service.OperationService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志实现类
 */
@Service
class OperationServiceImpl implements OperationService {


    @Autowired
    OperationMapper operationMapper;

    //分页
    @Override
    public Result queryOperationByPage(Integer pageNum, Integer pageSize, String admin_name) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Operation> data;
        if (StringUtil.isEmpty(admin_name)) {
            data = operationMapper.queryOperationByPage();
        } else {
            data = operationMapper.queryOperationByPageName(admin_name);
        }
        PageInfo<Partner> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Long operationSave(Operation operation) {
        return operationMapper.operationSave(operation);
    }

}
