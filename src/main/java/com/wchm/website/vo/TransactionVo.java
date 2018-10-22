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
@ApiModel(value = "通过区块哈希搜索返回的VO对象")
public class TransactionVo implements Serializable {

    private static final long serialVersionUID = -4266212573718429312L;

    @ApiModelProperty("区块哈希")
    private String hash;

    @ApiModelProperty("发送者")
    private String from;

    @ApiModelProperty("接受者")
    private String to;

    @ApiModelProperty("交易状态")
    private String state = "成功";

    @ApiModelProperty("区块高度")
    private BigInteger blockNumber;

    @ApiModelProperty("价值/交易额")
    private BigDecimal value;

    @ApiModelProperty("矿工费")
    private BigDecimal miner;

    @ApiModelProperty("日期")
    private String timestamp;
}
