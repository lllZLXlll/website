package com.wchm.website.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wchm.website.entity.Booking;
import com.wchm.website.mapper.BookingMapper;
import com.wchm.website.service.BookingService;
import com.wchm.website.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * 预售Service
 */
@Service
class BookingServiceImpl implements BookingService {

    @Autowired
    BookingMapper bookingMapper;

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


}
