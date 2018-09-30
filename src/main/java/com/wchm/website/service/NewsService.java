package com.wchm.website.service;

import com.wchm.website.entity.News;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public interface NewsService {

    List<News> queryNews();

    Result queryNewsByPage(Integer pageNum, Integer pageSize, String title);

    Result delNewsByID(Integer id);

    Result newsSave(News news);

    ModelAndView newsInfo(Integer id);

    Result newsInfoo(Long id);

    Result newsUpdate(News news);
}
