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
@ApiModel(value = "消息")
public class Message implements Serializable {

    private static final long serialVersionUID = -3886597091176783725L;

    @ApiModelProperty("ID")
    private Long id;


    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

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
