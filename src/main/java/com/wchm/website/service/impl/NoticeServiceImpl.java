package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wchm.website.entity.News;
import com.wchm.website.entity.Notice;
import com.wchm.website.mapper.NoticeMapper;
import com.wchm.website.service.NoticeService;
import com.wchm.website.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.List;
/*
公告Service
 */
@Service
class NoticeServiceImpl implements NoticeService {

    public final static Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public List<Notice> queryNotices() {
        return noticeMapper.queryNotices();
    }

    //公告分页
    @Override
    public Result queryNoticeList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Notice> data1;
        data1 = noticeMapper.queryNoticeList();
        PageInfo<Notice> p = new PageInfo(data1);
        return Result.create().success("查询成功", p);
    }
    //条件查询
    @Override
    public Result queryNoticeByPage(Integer pageNum, Integer pageSize, String title) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Notice> data;
        if (StringUtils.isEmpty(title)) {
            data = noticeMapper.queryNoticeByPage();
        } else {
            data = noticeMapper.queryNoticeByPageTitle(title);
        }
        PageInfo<Notice> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }
    //保存公告
    @Override
    public Result noticeSave(Notice notice) {
        log.info("------公告对象：" + notice);
        long result = noticeMapper.noticeSave(notice);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }
    //删除公告
    @Override
    public Result delNoticeByID(Integer id) {
        if (id == null || id <= 0) {
            return Result.create().fail("ID不能为空");
        }

        long result = noticeMapper.delNoticeByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }

        return Result.create().success("删除成功");
    }
    //插入公告
    @Override
    public ModelAndView noticeInfo(Integer id) {
        Notice notice = noticeMapper.noticeInfo(id);
        ModelAndView mav = new ModelAndView("notice-edit");
        mav.getModel().put("notice", notice);
        return mav;
    }
    //修改公告
    @Override
    public Result newsUpdate(Notice notice) {
        long result = noticeMapper.newsUpdate(notice);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }

    @Override
    public Result queryNoticeInfo(Long id) {
        Notice notice = noticeMapper.queryNoticeInfo(id);
        return Result.create().success("查询成功",notice);
    }


}
