package com.nikita.testproject.request;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Data
public class PasswordRequest {
    @Size(min = 5, max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9$@$!%*?&#^-_.+]+$", message = "password must contain english")
    private String password;
}
