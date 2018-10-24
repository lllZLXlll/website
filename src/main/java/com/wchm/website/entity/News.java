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
@ApiModel(value = "新闻")
public class News implements Serializable {

    private static final long serialVersionUID = 7757638151563725625L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("新闻时间")
    private Date time;

    @ApiModelProperty("新闻插入时间")
    private Date timeInsert;

    @ApiModelProperty("新闻图片")
    private String icon;

    @ApiModelProperty("新闻链接")
    private String url;

    @ApiModelProperty("新闻采集时间")
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
