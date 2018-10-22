package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wchm.website.entity.Message;
import com.wchm.website.entity.Notice;
import com.wchm.website.mapper.MessageMapper;
import com.wchm.website.service.MessageService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/*
公告Service
 */
@Service
class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Message> queryMessage() {
        return null;
    }

    @Override
    public Result queryMessageList(Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Result queryMessageInfo(Long id) {
        return null;
    }
    //条件查询
    @Override
    public Result queryMessageByPage(Integer pageNum, Integer pageSize, String title) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Message> data;
        if (StringUtils.isEmpty(title)) {
            data = messageMapper.queryMessageByPage();
        } else {
            data = messageMapper.queryMessageByPageTitle(title);
        }
        PageInfo<Notice> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    //保存
    @Override
    public Result messageSave(Message message) {
        long result = messageMapper.messageSave(message);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }
    //删除公告
    @Override
    public Result delMessageByID(Integer id) {
        if (id == null || id <= 0) {
            return Result.create().fail("ID不能为空");
        }
        long result = messageMapper.delMessageByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }

        return Result.create().success("删除成功");
    }
    //插入公告
    @Override
    public ModelAndView messageInfo(Integer id) {
        Message message = messageMapper.messageInfo(id);
        ModelAndView mav = new ModelAndView("message-edit");
        mav.getModel().put("message", message);
        return mav;
    }
    //修改公告
    @Override
    public Result messageUpdate(Message message) {
        long result = messageMapper.messageUpdate(message);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }




}
