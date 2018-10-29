package com.wchm.website.vo;

import com.wchm.website.entity.shiro.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户返回对象")
public class AdminVo implements Serializable {

    private static final long serialVersionUID = -4904576852587247346L;

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

    @ApiModelProperty("所有角色集合")
    private List<Role> roleList;

}
