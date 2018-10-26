package com.wchm.website.service;

import com.wchm.website.entity.News;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*
新闻接口
 */
@Service
public interface NewsService {

    List<News> queryNews();//
    //分页查询
    Result queryNewsByPage(Integer pageNum, Integer pageSize, String title,Integer lang);

    Result  queryNewsList(Integer pageNum, Integer pageSize,Integer lang);//
    //删除新闻
    Result delNewsByID(Integer id);
    //保存新闻
    Result newsSave(News news);
    //插入新闻
    ModelAndView newsInfo(Integer id);

    Result newsInfoo(Long id,Integer lang); //
    //修改新闻
    Result newsUpdate(News news);

    Result queryNewsInfo(Long id,Integer lang);



}
