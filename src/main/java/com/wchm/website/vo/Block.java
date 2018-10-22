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
@ApiModel(value = "首页区块VO")
public class Block implements Serializable, Comparable<Block> {

    private static final long serialVersionUID = -2634577741006835902L;

    @ApiModelProperty("区块号")
    private BigInteger blockNumber;

    @ApiModelProperty("哈希")
    private String hash;

    @ApiModelProperty("难度")
    private BigInteger totalDifficulty;

    @ApiModelProperty("时间戳")
    private BigInteger timeStamp;

    @Override
    public int compareTo(Block b) {
        return b.getBlockNumber().intValue() - this.blockNumber.intValue();
    }
}
