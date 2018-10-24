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

    @ApiModelProperty("英文名")
    private String english_name;

    @ApiModelProperty("中文描述")
    private String description;

    @ApiModelProperty("链接")
    private String link;

    @ApiModelProperty("关注数量")
    private Integer follow_number;

    @ApiModelProperty("是否展示")
    private Integer state;

}
