package org.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}