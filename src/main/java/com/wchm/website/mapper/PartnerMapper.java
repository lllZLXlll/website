package com.wchm.website.mapper;

import com.wchm.website.entity.Partner;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PartnerMapper {


  /*
   @Select("SELECT * FROM team WHERE id = #{id}")
    Team queryteamInfo(@Param("id") Long id);*/

    @Select("SELECT * FROM website_partner WHERE state = 1 ORDER BY create_time DESC ")
    List<Partner> queryPartner();

    @Select("SELECT * FROM website_partner ORDER BY create_time DESC")
    List<Partner> queryPartnerByPage();

    //查询
    @Select("SELECT * FROM website_partner WHERE partner_name LIKE '%' #{partner_name} '%' ORDER BY create_time DESC")
    List<Partner> queryPartnerByPageTitle(@Param("partner_name") String partner_name);

    //删除
    @Delete("DELETE FROM website_partner WHERE id = #{id}")
    Long delPartnerByID(@Param("id") Integer id);

    //插入
    @Insert("INSERT INTO website_partner(partner_name, number, picture, link, state,create_time) " +
            "VALUES(#{partner.partner_name}, #{partner.number}, #{partner.picture}, #{partner.link}, #{partner.state},#{partner.create_time})")
    Long partnerSave(@Param("partner") Partner partner);

    @Select("SELECT * FROM website_partner WHERE id = #{id}")
    Partner partnerInfo(@Param("id") Integer id);

    //修改
    @Update(" UPDATE website_partner SET " +
            "  partner_name = #{partner.partner_name},number = #{partner.number}, " +
            " picture = #{partner.picture}, link = #{partner.link}, " +
            " state = #{partner.state},create_time = #{partner.create_time} " +
            " where id = #{partner.id} ")
    Long partnerUpdate(@Param("partner") Partner partner);

    @Select("SELECT * FROM website_news WHERE id = #{id}")
    Partner queryPartnerInfo(@Param("id") Long id);
}
