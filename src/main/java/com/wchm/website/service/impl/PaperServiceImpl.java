package com.wchm.website.service.impl;

import com.wchm.website.mapper.BookingMapper;
import com.wchm.website.service.PaperService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 白皮书ServiceImpl
 */
@Service
class PaperServiceImpl implements PaperService {

    @Autowired
    BookingMapper bookingMapper;

    @Value("${wchm.paper-absolutely}")
    private String paperAbsolutely;

    @Override
    public Result paperSave(String number, HttpServletResponse response,
                            HttpServletRequest request) throws IOException {

        String filename;

        if (StringUtils.isEmpty(number)) {
            number = "2";
        }

        if (number.equals("1")) {
            filename = "Chinese.pdf";
        } else {
            filename = "English.pdf";
        }
        //指定路径
        String filePath = paperAbsolutely + filename;

        // 输入流
        FileInputStream in = new FileInputStream(filePath);
        response.setCharacterEncoding("utf-8");

        if (request.getHeader("user-agent").toLowerCase().contains("msie")) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
        }

        // 设置头
        response.setHeader("content-disposition", "attachment;filename=" + filename);
        //文件设置
        response.setHeader("content-type", "application/octet-stream");
        // 获取绑定了客户端的流
        ServletOutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        in.close();
        return Result.create().success("下载成功");
    }

}

