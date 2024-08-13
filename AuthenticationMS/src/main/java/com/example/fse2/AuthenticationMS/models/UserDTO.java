package com.example.fse2.AuthenticationMS.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotEmpty
    private String userName;
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 8, message = "Password should minimum 8 letters")
    private String password;

    @NotEmpty
    private String role;
}
