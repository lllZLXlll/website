package com.wchm.website.entity;

import com.wchm.website.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "代币池")
public class Currency implements Serializable {

    private static final long serialVersionUID = 5269095442131149243L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String user_name;

    @ApiModelProperty("客户手机号码")
    private String mobile;

    @ApiModelProperty("用户钱包地址")
    private String address;

    @ApiModelProperty("用户代币总额")
    private BigDecimal currency;

    @ApiModelProperty("用户剩余代币总额")
    private BigDecimal surplus;

    @ApiModelProperty("锁仓描述")
    private String lock_describe;

    @ApiModelProperty("锁仓比例")
    private Integer proportion;

    @ApiModelProperty("锁仓开始时间")
    private Date lock_begin_time;

    @ApiModelProperty("锁仓结束时间")
    private Date lock_end_time;

    @ApiModelProperty("上一次解仓时间")
    private Date last_unlock_time;

    @ApiModelProperty("用户代币备注")
    private String remarks;

    @ApiModelProperty("是否有效（ 1有效 0无效）")
    private Integer state = 1;

    @ApiModelProperty("创建时间")
    private Date create_time = new Date();

    public String getCreate_time() {
        return DateUtil.formatDefaultDate(create_time);
    }


    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", currency=" + currency +
                ", surplus=" + surplus +
                ", lock_describe='" + lock_describe + '\'' +
                ", proportion=" + proportion +
                ", lock_begin_time=" + lock_begin_time +
                ", lock_end_time=" + lock_end_time +
                ", last_unlock_time=" + last_unlock_time +
                ", remarks='" + remarks + '\'' +
                ", state=" + state +
                ", create_time=" + create_time +
                '}';
    }
}
