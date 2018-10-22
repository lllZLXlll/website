package com.wchm.website.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "搜索的详情信息VO")
public class DetailsVo implements Serializable {

    private static final long serialVersionUID = -6613241065241993396L;

    @ApiModelProperty("通过区块哈希搜索-交易详情")
    private TransactionVo transactionVo;

    @ApiModelProperty("通过钱包地址搜索-账户地址")
    private String address;

    @ApiModelProperty("通过钱包地址搜索-余额")
    private BigDecimal balance;

    @ApiModelProperty("通过钱包地址搜索-交易笔数")
    private BigInteger txCount;

    @ApiModelProperty("通过钱包地址搜索-交易记录集合对象")
    private List<TransactionRecordVo> transactionRecordVo;


}
