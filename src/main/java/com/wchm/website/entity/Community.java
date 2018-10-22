package com.wchm.website.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "关注")
public class Community implements Serializable {

    private static final long serialVersionUID = -5982061115516047032L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("第1个关注人数")
    private Integer count1;

    @ApiModelProperty("第2个关注人数")
    private Integer count2;

    @ApiModelProperty("第3个关注人数")
    private Integer count3;

    @ApiModelProperty("第4个关注人数")
    private Integer count4;


}
