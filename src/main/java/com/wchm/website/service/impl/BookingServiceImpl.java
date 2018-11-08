package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Booking;
import com.wchm.website.mapper.BookingMapper;
import com.wchm.website.service.BookingService;
import com.wchm.website.util.Result;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 预售Service
 */
@Service
class BookingServiceImpl implements BookingService {

    @Autowired
    BookingMapper bookingMapper;

    @Autowired
    BookingService bookingService;

    //条件查询
    @Override
    public Result queryBookingByPage(Integer pageNum, Integer pageSize, String user_name) {
        PageHelper.startPage(pageNum == null || pageNum <= 0 ? 1 : pageNum, pageSize == null || pageSize <= 0 ? 10 : pageSize);
        List<Booking> data;
        if (StringUtils.isEmpty(user_name)) {
            data = bookingMapper.queryBookingByPage();
        } else {
            data = bookingMapper.queryBookingByPageName(user_name);
        }
        PageInfo<Booking> p = new PageInfo(data);
        return Result.create().success("查询成功", p);
    }


    @Override
    public Result bookingSave(Booking booking) {
        if (StringUtil.isEmpty(booking.getUser_name())) {
            return Result.create().fail("名字不能为空！");
        }
        if (StringUtil.isEmpty(booking.getSur_name())) {
            return Result.create().fail("姓氏不能为空！");
        }
        if (StringUtil.isEmpty(booking.getEmail())) {
            return Result.create().fail("邮箱不能为空！");
        }
        if (StringUtil.isEmpty(booking.getAddress())) {
            return Result.create().fail("钱包地址不能为空");
        }
        if (booking.getInvestment() == null) {
            return Result.create().fail("投资方式不能为空！");
        }
        long result = bookingMapper.bookingSave(booking);
        if (result <= 0) {
            return Result.create().fail("添加失败");
        }
        return Result.create().success("添加成功");
    }


    /**
     * Excel 导出
     * @return
     */
    @Override
    public List<Booking> bookingInfor() {
        return bookingMapper.bookingInfor();
    }

    /**
     * 预售表单Excel 导出
     * @return
     */
    @Override
    public Object bookingExport(HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("预售表单");
        // 查询数据库
        List<Booking> classmateList = bookingService.bookingInfor();
        // 设置要导出的文件的名字
        String fileName = "预售表单" + ".xls";
        // 新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"序号","姓氏" , "名字", "手机号码", "邮箱", "钱包地址",
                "创建时间", "投资方式", "预售投资金额", "投资货币", "电脑账号",
                "所在国家", "parkEco", "反馈意见"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);//目的是想把行高设置成20px

        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setFontName("黑体");
        fontStyle.setFontHeightInPoints((short) 20);
        //  在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        int i =1;
        // 在表中存放查询到的数据放入对应的列
        for (Booking booking : classmateList) {
            // 投资方式（1.个人投资/2.基金投资）
            String investment = booking.getInvestment();
            if (investment.equals("1")) {
                investment = "个人投资";
            } else if (investment.equals("2")) {
                investment = "基金投资";
            }

            // 投资货币(1.BTC 2.ETH 3.TUSD)
            String currency = booking.getCurrency();
//            if (currency.equals("1")) {
//                currency = "BTC";
//            } else if (currency.equals("2")) {
//                currency = "ETH";
//            } else if (currency.equals("3")) {
//                currency = "TUSD";
//            }
            //park_eco"parkEco(1.口口相传2.电报 3.媒体出版物 4.互联网 5.一次会议 6.我们目前投资者之一 7.其他)"
            String park_eco = booking.getPark_eco();
//            if(park_eco.equals("1")){
//                park_eco="口口相传";
//            }else if(park_eco.equals("2")){
//                park_eco="电报";
//            }else if(park_eco.equals("3")){
//                park_eco="媒体出版物";
//            }else if(park_eco.equals("4")){
//                park_eco="互联网";
//            }else if(park_eco.equals("5")){
//                park_eco="一次会议";
//            }else if(park_eco.equals("6")){
//                park_eco="我们目前投资者之一";
//            }else if(park_eco.equals("7")){
//                park_eco="其他";
//            }

            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(i++);
            row1.createCell(1).setCellValue(booking.getUser_name());
            row1.createCell(2).setCellValue(booking.getSur_name());
            row1.createCell(3).setCellValue(booking.getMobile());
            row1.createCell(4).setCellValue(booking.getEmail());
            row1.createCell(5).setCellValue(booking.getAddress());
            row1.createCell(6).setCellValue(booking.getCreate_time());
            //    row1.createCell(7).setCellValue(booking.getState()+"");
            row1.createCell(7).setCellValue(investment);
            row1.createCell(8).setCellValue(booking.getDollar());
            row1.createCell(9).setCellValue(currency);
            row1.createCell(10).setCellValue(booking.getAccount());
            row1.createCell(11).setCellValue(booking.getCountry());
            row1.createCell(12).setCellValue(park_eco);
            row1.createCell(13).setCellValue(booking.getFeedback());

            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return null;
    }








}
