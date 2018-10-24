package com.wchm.website.service;

import com.wchm.website.entity.Notice;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public interface NoticeService {

    List<Notice> queryNotices();

    Result  queryNoticeList(Integer pageNum, Integer pageSize,Integer lang);

    Result queryNoticeByPage(Integer pageNum, Integer pageSize, String title,Integer lang);

    Result noticeSave(Notice notice);

    Result delNoticeByID(Integer id);

    ModelAndView noticeInfo(Integer id);

    Result newsUpdate(Notice notice);

    Result queryNoticeInfo(Long id,Integer lang);
}
