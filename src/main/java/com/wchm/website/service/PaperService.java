package com.wchm.website.service;

import com.wchm.website.entity.Booking;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public interface PaperService {

    Result paperSave(String number, HttpServletResponse response,
                     HttpServletRequest request) throws IOException;


}
