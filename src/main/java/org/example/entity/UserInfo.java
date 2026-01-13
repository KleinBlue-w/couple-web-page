package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("user_info")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    // 用户名
    @TableField("user_name")
    private String userName;

    // 密码
    @TableField("user_password")
    private String userPassword;

    // 绑定账号
    @TableField("bind_account")
    private String bindAccount;

    // 账号
    @TableField("user_account")
    private String userAccount;

    // 插入时自动写入创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时自动写入更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime modifyTime;

    // 绑定手机号码
    @TableField("tel_phone")
    private String telPhone;

    // 头像
    @TableField("avatar")
    private String avatar;
}
