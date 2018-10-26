package com.wchm.website.service;

import com.wchm.website.entity.Message;
import com.wchm.website.entity.Notice;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public interface MessageService {

    List<Message> queryMessage();//

    Result queryMessageList(Integer pageNum, Integer pageSize);//

    Result queryMessageByPage(Integer pageNum, Integer pageSize, String title);

    Result messageSave(Message message);

    Result delMessageByID(Integer id);

    ModelAndView messageInfo(Integer id);

    Result messageUpdate(Message message);


}