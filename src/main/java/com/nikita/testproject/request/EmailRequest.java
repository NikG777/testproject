package com.nikita.testproject.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class EmailRequest {
    @Email
    @Size(min = 5, max = 19)
    private String email;

}
