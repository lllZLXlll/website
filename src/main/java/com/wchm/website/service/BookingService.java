package com.wchm.website.service;

import com.wchm.website.entity.Booking;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {

    Result queryBookingByPage(Integer pageNum, Integer pageSize, String user_name);

    //保存
    Result bookingSave(Booking booking);
}
