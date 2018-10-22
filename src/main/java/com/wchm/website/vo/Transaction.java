package com.wchm.website.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "首页区块交易VO")
public class Transaction implements Serializable {

    private static final long serialVersionUID = -5048141823485662965L;

    @ApiModelProperty("哈希")
    private String hash;

    @ApiModelProperty("区块号")
    private BigInteger blockNumber;

    @ApiModelProperty("时间戳")
    private BigInteger timeStamp;

}
