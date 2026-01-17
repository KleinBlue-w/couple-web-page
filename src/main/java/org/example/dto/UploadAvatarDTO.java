package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UploadAvatarDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotBlank(message = "头像数据不能为空")
    private String url;  // Base64字符串
}