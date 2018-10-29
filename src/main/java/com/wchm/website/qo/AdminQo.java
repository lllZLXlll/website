package com.wchm.website.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户请求对象")
public class AdminQo implements Serializable {

    private static final long serialVersionUID = 7378187502422162944L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("移动电话")
    private String mobile;

    @ApiModelProperty("启用:1，禁用:0")
    private Integer state;

    @ApiModelProperty("角色ID")
    private Long roleId;

}
