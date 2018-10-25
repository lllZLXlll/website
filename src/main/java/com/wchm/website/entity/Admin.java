package com.wchm.website.entity;

import com.wchm.website.entity.shiro.Role;
import com.wchm.website.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "管理员")
public class Admin implements Serializable {

    private static final long serialVersionUID = 7727618151563725625L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("创建时间")
    private Date create_time;

    //用户的角色   一对多关系
    private List<Role> roleList;

    public String getCreate_time() {
        return DateUtil.formatDefaultDate(create_time);

    }
}
