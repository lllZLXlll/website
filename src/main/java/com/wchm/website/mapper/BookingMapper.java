package com.wchm.website.mapper;

import com.wchm.website.entity.Booking;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookingMapper {

    @Select("SELECT * FROM website_advance_booking ORDER BY create_time DESC")
    List<Booking> queryBookingByPage();

    @Select("SELECT * FROM website_advance_booking WHERE user_name LIKE '%' #{user_name} '%' ORDER BY create_time DESC")
    List<Booking> queryBookingByPageName(@Param("user_name") String user_name);

    //插入
    @Insert("INSERT INTO website_advance_booking(" +
            "   user_name, sur_name, mobile, email, address," +
            "   create_time,state,investment ,dollar,currency," +
            "   account,country,park_eco,feedback )" +
            "VALUES(" +
            "   #{booking.user_name}, #{booking.sur_name}, #{booking.mobile}, #{booking.email}, #{booking.address}, #{booking.create_time},"+
            "   #{booking.state}, #{booking.investment}, #{booking.dollar}, #{booking.currency}, #{booking.account}, #{booking.country},"+
            "   #{booking.park_eco}, #{booking.feedback})")
    Long bookingSave(@Param("booking") Booking booking);

    // 查询导出Excel数据
    @Select("select * from website_advance_booking ORDER BY create_time DESC")
     List<Booking> bookingInfor();
}
