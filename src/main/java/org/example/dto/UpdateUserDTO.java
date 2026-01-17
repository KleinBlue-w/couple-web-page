package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "性别不能为空")
    private String gender;

    @NotBlank(message = "出生日期不能为空")
    private String birthday;

    // 移除 @NotBlank 注解，允许空字符串
    private String bindAccount;
}