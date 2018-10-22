package com.wchm.website.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通过钱包地址搜索返回的VO对象")
public class TransactionRecordVo implements Serializable {

    private static final long serialVersionUID = 3370428979218018171L;

    @ApiModelProperty("类型：receive/send")
    private String type;

    @ApiModelProperty("账户")
    private String address;

    @ApiModelProperty("价值/交易额")
    private BigDecimal value;

    @ApiModelProperty("矿工费")
    private BigDecimal miner;

    @ApiModelProperty("日期，没有日期就是Unknown")
    private String timestamp;

    @ApiModelProperty("区块哈希")
    private String hash;
}
