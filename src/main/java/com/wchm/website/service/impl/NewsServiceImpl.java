package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wchm.website.entity.News;
import com.wchm.website.mapper.NewsMapper;
import com.wchm.website.service.NewsService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.util.List;
/*
 新闻Service
 */
@Service
class NewsServiceImpl implements NewsService {

    @Autowired
    NewsMapper newsMapper;

    @Override
    public List<News> queryNews() {
        return newsMapper.queryNews();
    }
    //新闻分页
    @Override
    public Result queryNewsByPage(Integer pageNum, Integer pageSize, String title,Integer lang) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<News> data;
        if (StringUtils.isEmpty(title)) {
            data = newsMapper.queryNewsByPage();
        } else {
            data = newsMapper.queryNewsByPageTitle(title,lang);
        }
        PageInfo<News> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }
    //删除新闻
    @Override
    public Result delNewsByID(Integer id) {
        if (id == null || id <= 0) {
            return Result.create().fail("ID不能为空");
        }

        long result = newsMapper.delNewsByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");
        }

        return Result.create().success("删除成功");
    }
    //保存新闻
    @Override
    public Result newsSave(News news) {
        long result = newsMapper.newsSave(news);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }
    //插入新闻
    @Override
    public ModelAndView newsInfo(Integer id) {
        News news = newsMapper.newsInfo(id);
        ModelAndView mav = new ModelAndView("news-edit");
        mav.getModel().put("news", news);
        return mav;
    }

    //修改新闻
    @Override
    public Result newsUpdate(News news) {
        long result = newsMapper.newsUpdate(news);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }

    @Override
    public Result newsInfoo(Long id) {
        News news= newsMapper.queryNewsInfo(id);
        return Result.create().success("查询成功", news);
    }

}
