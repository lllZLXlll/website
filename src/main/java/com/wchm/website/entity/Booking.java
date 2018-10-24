package com.wchm.website.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wchm.website.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "预售")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1984265534158289544L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("名字")
    @JsonProperty("user_name")
    private String user_name;

    @ApiModelProperty("姓氏")
    @JsonProperty("sur_name")
    private String sur_name;

    @ApiModelProperty("手机号")
    @JsonProperty("mobile")
    private String mobile;

    @ApiModelProperty("邮箱")
    @JsonProperty("email")
    private String email;

    @ApiModelProperty("钱包地址")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty("创建时间")
    @JsonProperty("create_time")
    private Date create_time;

    @ApiModelProperty("状态（1有效，0无效）")
    @JsonProperty("state")
    private Integer state;

    @ApiModelProperty("投资方式（1.个人投资/2.基金投资）")
    @JsonProperty("investment")
    private String investment;

    @ApiModelProperty("预售投资金额")
    @JsonProperty("dollar")
    private String dollar;

    @ApiModelProperty("投资货币(1.BTC 2.ETH 3.TUSD)")
    @JsonProperty("currency")
    private String currency;

    @ApiModelProperty("电脑账号")
    @JsonProperty("account")
    private String account;

    @ApiModelProperty("所在国家")
    @JsonProperty("country")
    private String country;

    @ApiModelProperty("parkEco(1.口口相传2.电报 3.媒体出版物 4.互联网 5.一次会议 6.我们目前投资者之一 7.其他)")
    @JsonProperty("park_eco")
    private String park_eco;

    @ApiModelProperty("反馈意见")
    @JsonProperty("feedback")
    private String feedback;

    public String getCreate_time() {
        return DateUtil.formatTimesTampDate(create_time);
    }
}
