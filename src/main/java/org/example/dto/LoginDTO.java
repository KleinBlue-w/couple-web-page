package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank
    private String userAccount;
    @NotBlank
    private String password;

    private String bindAccount;
}