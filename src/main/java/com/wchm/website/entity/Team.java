package com.wchm.website.entity;

import com.wchm.website.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "团队")
public class Team implements Serializable {

    private static final long serialVersionUID = -3886597091176783725L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("序号")
    private String number;

    @ApiModelProperty("成员名称")
    private String team_name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("头像地址")
     private String head;

    @ApiModelProperty("创建时间")
    private Date create_time;

    @ApiModelProperty("是否展示（1:展示，0:不展示）")
    private Integer state;

    public String getTime() {
        return DateUtil.formatDefaultDate(create_time);
    }

    public String getCreate_time() {
        return DateUtil.formatDefaultDate(create_time);

    }
}
