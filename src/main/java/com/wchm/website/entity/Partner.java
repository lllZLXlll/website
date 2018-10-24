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
@ApiModel(value = "合作伙伴")
public class Partner implements Serializable {

    private static final long serialVersionUID = 8230666313790210175L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("序号")
    private String number;

    @ApiModelProperty("合作伙伴名称")
    private String partner_name;

    @ApiModelProperty("图片")
    private String picture;

    @ApiModelProperty("链接")
     private String link;

    @ApiModelProperty("创建时间")
    private Date create_time;

    @ApiModelProperty("是否展示（1:展示，0:不展示）")
    private Integer state;

    public String getTime() {
        return DateUtil.formatTimesTampDate(create_time);
    }

    public String getCreate_time() {
        return DateUtil.formatTimesTampDate(create_time);

    }
}
