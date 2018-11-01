package com.wchm.website.vo;

import com.wchm.website.entity.ExtractApplyfor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "代币池用户锁仓信息")
public class CurrencyAccountVo implements Serializable {

    private static final long serialVersionUID = -3620074420575421519L;

    @ApiModelProperty("持币总数")
    private BigDecimal currency;

    @ApiModelProperty("可用持币")
    private BigDecimal surplus;

    @ApiModelProperty("锁仓比例")
    private Integer proportion;

    @ApiModelProperty("锁仓时间")
    private String lock_time;

    @ApiModelProperty("锁仓计划")
    private String lock_describe;

    @ApiModelProperty("用户钱包地址")
    private String address;

    @ApiModelProperty("提现记录")
    private List<ExtractApplyfor> extractList;

}
