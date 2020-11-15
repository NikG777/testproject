package com.nikita.testproject.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserAccRequest {
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9$@$!%*?&#^-_.+]+$", message = "login must contain english")
    private String login;
    @Size(min = 5, max = 8)
    @Pattern(regexp = "^[a-zA-Z0-9$@$!%*?&#^-_.+]+$", message = "password must contain english")
    private String password;
    @Email
    @Size(min = 5, max = 30)
    private String email;

    public UserAccRequest(@Size(min = 4, max = 10) @Pattern(regexp = "^[a-zA-Z0-9$@$!%*?&#^-_.+]+$", message = "login must contain english") String login, @Size(min = 5, max = 8) @Pattern(regexp = "^[a-zA-Z0-9$@$!%*?&#^-_.+]+$", message = "password must contain english") String password, @Email @Size(min = 5, max = 30) String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}