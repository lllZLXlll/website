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
@ApiModel(value = "公告")
public class Notice implements Serializable {

    private static final long serialVersionUID = 4157282236786835771L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("公告时间")
    private Date time;

    @ApiModelProperty("公告插入时间")
    private Date timeInsert;

    @ApiModelProperty("公告采集时间")
    private Date create_time;

    @ApiModelProperty("是否展示（1:展示，0:不展示）")
    private Integer state;

    @ApiModelProperty("语言（1:英文，0:中文）")
    private Integer lang;

    public String getTime() {
        return DateUtil.formatDefaultDate(time);
    }

    public String getCreate_time() {
        return DateUtil.formatDefaultDate(create_time);

    }
}
