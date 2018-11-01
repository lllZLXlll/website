package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
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


    //新闻分页
    @Override
    public Result queryNewsByPage(Integer pageNum, Integer pageSize, String title,Integer lang) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<News> data;
        if (StringUtils.isEmpty(title)&& lang==null) {
            data = newsMapper.queryNewsByPage();
        } else {
            data = newsMapper.queryNewsByPageTitle(title,lang);
        }
        PageInfo<News> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }

    @Override
    public Result queryNewsList(Integer pageNum, Integer pageSize, Integer lang) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<News> data1;
        data1 = newsMapper.queryNewsList(lang);
        PageInfo<News> p = new PageInfo(data1);
        return Result.create().success("查询成功", p);
    }

    //删除新闻
    @Override
    public Result delNewsByID(Integer id){
        if (id == null || id <= 0) {
            return Result.create().fail("ID不能为空");
        }
        long  result = newsMapper.delNewsByID(id);
        if (result <= 0) {
            return Result.create().fail("删除失败");

        }
        return Result.create().success("删除成功");

    }

    //保存新闻
    @Override
    public Result newsSave(News news) {
        if(StringUtil.isEmpty(news.getTitle())){
            return Result.create().fail("消息标题不能为空");
        }
        if(StringUtil.isEmpty(news.getContent())){
            return Result.create().fail("内容不能为空");
        }
    /*    if (StringUtil.isEmpty(news.getCreate_time())){
            return Result.create().fail("时间不能为空！");
        }*/
        if(StringUtil.isEmpty(news.getIcon())){
            return Result.create().fail("图片地址不能为空");
        }
        if(StringUtil.isEmpty(news.getUrl())){
            return Result.create().fail("原文地址不能为空");

        }

        long result = newsMapper.newsSave(news);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }

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
        if(StringUtil.isEmpty(news.getTitle())){
            return Result.create().fail("消息标题不能为空");
        }
        if(StringUtil.isEmpty(news.getContent())){
            return Result.create().fail("内容不能为空");
        }
      /*  if (StringUtil.isEmpty(news.getCreate_time())){
            return Result.create().fail("时间不能为空！");
        }*/

        if(StringUtil.isEmpty(news.getIcon())){
            return Result.create().fail("图片地址不能为空");
        }
        if(StringUtil.isEmpty(news.getUrl())){
            return Result.create().fail("原文地址不能为空");
        }
        long result = newsMapper.newsUpdate(news);
        if (result <= 0) {
            return Result.create().fail("修改失败");
        }
        return Result.create().success("修改成功");
    }

    @Override
    public Result queryNewsInfo(Long id, Integer lang) {
        News news= newsMapper.queryNewsInfo(id,lang);
        return Result.create().success("查询成功",news);
    }

    @Override
    public Result newsInfoo(Long id, Integer lang) {
        News news= newsMapper.queryNewsInfo(id,lang);
        return Result.create().success("查询成功", news);
    }

    @Override
    public List<News> queryNews() {
        return newsMapper.queryNews();
    }

}
