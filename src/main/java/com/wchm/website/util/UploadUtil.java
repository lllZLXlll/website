package com.wchm.website.util;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class UploadUtil {
    public final static Logger log = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 上传图片
     *
     * @param request
     * @return 图片路径
     * @throws Exception
     */
    @PostMapping(value = "/imageUpload")
    @ResponseBody
    public static String imageUpload(HttpServletRequest request, String relative, String absolutely) throws Exception {

        // 获取图片保存目录
        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }

        // 文件保存绝对地址
        String savePath = absolutely + dirName + "/";

        // 文件保存相对地址
        String saveUrl = request.getContextPath() + relative + dirName + "/";

        log.info("--------------savePath----------------" + savePath);
        log.info("--------------saveUrl----------------" + saveUrl);

        // 定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

        // 检查目录,如果不存在就创建一个目录
        File uploadDir = new File(savePath);
        if (!uploadDir.isDirectory()) {
            uploadDir.mkdirs();
        }

        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath += ymd + "/";
        saveUrl += ymd + "/";
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        Iterator item = multipartRequest.getFileNames();
        while (item.hasNext()) {
            String fileName = (String) item.next();
            MultipartFile file = multipartRequest.getFile(fileName);
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            try {
                //开始上传
                File uploadedFile = new File(savePath, newFileName);
                ByteStreams.copy(file.getInputStream(), new FileOutputStream(uploadedFile));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return saveUrl + newFileName;
        }
        return null;
    }
}
