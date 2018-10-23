package com.wchm.website.entity;

import com.wchm.website.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "代币池交易记录")
public class CurrencyRecord implements Serializable {

    private static final long serialVersionUID = -7384905003491243030L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("带币池表id，关联外键")
    private Long pool_id;

    @ApiModelProperty("支出账户地址")
    private String from;

    @ApiModelProperty("收入账户地址")
    private String to;

    @ApiModelProperty("交易地址")
    private String txAddress;

    @ApiModelProperty("用户代币总额")
    private BigDecimal currency;

    @ApiModelProperty("期数，从第1期开始增加")
    private Integer periods;

    @ApiModelProperty("交易状态 1:成功，0:失败")
    private Integer state;

    @ApiModelProperty("交易描述，如失败写交易失败原因")
    private String describe;

    @ApiModelProperty("转账操作人")
    private String admin;

    @ApiModelProperty("转账时间")
    private Date time;

    public String getTime() {
        return DateUtil.formatDefaultDate(time);
    }

    public String getTxAddress() {
        if (StringUtils.isEmpty(txAddress))
            return "";
        return txAddress;
    }
}
