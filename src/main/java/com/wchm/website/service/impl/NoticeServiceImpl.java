package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.News;
import com.wchm.website.entity.Notice;
import com.wchm.website.mapper.NoticeMapper;
import com.wchm.website.service.NoticeService;
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
class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public List<Notice> queryNotices() {
        return noticeMapper.queryNotices();
    }


    @Override
    public Result queryNoticeList(Integer pageNum, Integer pageSize,Integer lang) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Notice> data1;
        data1 = noticeMapper.queryNoticeList(lang);
        PageInfo<Notice> p = new PageInfo(data1);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Result queryNoticeByPage(Integer pageNum, Integer pageSize, String title,Integer lang) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Notice> data;
        if (StringUtils.isEmpty(title)&& lang==null) {
            data = noticeMapper.queryNoticeByPage();
        } else {
            data = noticeMapper.queryNoticeByPageTitle(title,lang);
        }
        PageInfo<Notice> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Result noticeSave(Notice notice) {

        if(StringUtil.isEmpty(notice.getTitle())){
            return Result.create().fail("公告标题不能为空");
        }

        if(StringUtil.isEmpty(notice.getContent())){
            return Result.create().fail("公告内容不能为空");
        }
        if(StringUtil.isEmpty(notice.getDescription())){
            return Result.create().fail("公告描述不能为空");
        }
      /*  if(notice.getTime()==null){
            return Result.create().fail("公告时间不能为空");
        }*/

        long result = noticeMapper.noticeSave(notice);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }

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

    @Override
    public ModelAndView noticeInfo(Integer id) {
        Notice notice = noticeMapper.noticeInfo(id);
        ModelAndView mav = new ModelAndView("notice-edit");
        mav.getModel().put("notice", notice);
        return mav;
    }

    @Override
    public Result newsUpdate(Notice notice) {

        if(StringUtil.isEmpty(notice.getTitle())){
            return Result.create().fail("公告标题不能为空");
        }

        if(StringUtil.isEmpty(notice.getContent())){
            return Result.create().fail("公告内容不能为空");
        }
        if(StringUtil.isEmpty(notice.getDescription())){
            return Result.create().fail("公告描述不能为空");
        }
      /*  if(notice.getTime()==null){
            return Result.create().fail("公告时间不能为空");
        }*/
        long result = noticeMapper.newsUpdate(notice);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }


    @Override
    public Result queryNoticeInfo(Long id, Integer lang) {
        Notice notice = noticeMapper.queryNoticeInfo(id,lang);
        return Result.create().success("查询成功",notice);
    }


}
