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
@ApiModel(value = "提现申请")
public class ExtractApplyfor implements Serializable {

    private static final long serialVersionUID = 3859045813388423916L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("uid")
    private Long uid;

    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("钱包地址")
    private String address;

    @ApiModelProperty("提现金额")
    private BigDecimal money;

    @ApiModelProperty("用户剩余代币总额")
    private BigDecimal currency;

    @ApiModelProperty("管理员确认提现时间")
    private Date confirm_time;

    @ApiModelProperty("提现申请时间")
    private Date time;

    @ApiModelProperty("状态：0:申请提现中，1:提现成功")
    private Integer state;

    public String getTime() {
        return DateUtil.formatDefaultDate(time);

    }

    public String getConfirm_time() {
        return DateUtil.formatDefaultDate(confirm_time);

    }

}
