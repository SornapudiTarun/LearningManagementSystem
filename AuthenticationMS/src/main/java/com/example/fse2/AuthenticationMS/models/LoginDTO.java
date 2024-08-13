package com.example.fse2.AuthenticationMS.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
