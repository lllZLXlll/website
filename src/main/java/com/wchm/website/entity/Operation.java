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
@ApiModel(value = "操作日志")
public class Operation implements Serializable {

    private static final long serialVersionUID = -5685994756409556656L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("管理员")
    private String admin_name;

    @ApiModelProperty("操作类型")
    private String operation_type;

    @ApiModelProperty("操作时间")
    private Date create_time;

    @ApiModelProperty("金额")
    private BigDecimal money;

    @ApiModelProperty("用户钱包地址")
    private String address;

    @ApiModelProperty("状态（ 1成功 0失败）")
    private Integer state;

    @ApiModelProperty("方法名")
    private String method;

    @ApiModelProperty("参数")
    private String params;



    public String getCreate_time() {
        return DateUtil.formatTimesTampDate(create_time);

    }
    public Object getMoney() {
        if (money==null){
            return  "";
        }
        return money;
    }


    public String getAddress() {
        if(address==null){
            return "";
        }
        return address;
    }

}
