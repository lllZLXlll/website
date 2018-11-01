package com.wchm.website.service;

import com.wchm.website.entity.Booking;
import com.wchm.website.util.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface BookingService {

    Result queryBookingByPage(Integer pageNum, Integer pageSize, String user_name);

    //保存
    Result bookingSave(Booking booking);


    List<Booking> bookingInfor();

    Object bookingExport(HttpServletResponse response) throws IOException;
}
