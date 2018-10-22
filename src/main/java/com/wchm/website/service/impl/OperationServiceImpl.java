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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 操作日志实现类
 *
 */
@Service
class OperationServiceImpl implements OperationService {



    @Autowired
    OperationMapper operationMapper;

    //前端接口
    @Override
    public List<Operation> queryOperation() {
        return operationMapper.queryOperation();
    }


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

    //保存
    @Override
    public Result operationSave(Operation operation) {
        long result = operationMapper.operationSave(operation);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }
    //删除
    @Override
    public Result delOperationByID(Integer id) {
        long result = operationMapper.delOperationByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }
        return Result.create().success("删除成功");
    }

    //
    @Override
    public ModelAndView operationInfo(Integer id) {
        Operation operation = operationMapper.operationInfo(id);
        ModelAndView mav = new ModelAndView("operation-edit");
        mav.getModel().put("operation", operation);
        return mav;
    }
    //修改
    @Override
    public Result operationUpdate(Operation operation) {
        long result = operationMapper.operationUpdate(operation);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }


}
