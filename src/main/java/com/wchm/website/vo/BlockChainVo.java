package com.wchm.website.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "区块链首页数据返回对象")
public class BlockChainVo implements Serializable {

    private static final long serialVersionUID = -2377518751218171249L;

    @ApiModelProperty("最新交易记录集合")
    private List<Transaction> tranList;

//    @ApiModelProperty("最新区块集合")
//    private Set<Block> blockList;

    @ApiModelProperty("发行总量")
    private Long total = 1000000000l;

    @ApiModelProperty("目前流通量")
    private Long amount = 200000000l;

    @ApiModelProperty("地址")
    private Long addressCount = 846l;

    @ApiModelProperty("当前价格：单位美元")
    private BigDecimal price = new BigDecimal("0.172");

}
