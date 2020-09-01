package com.example.photoappapiusers.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class User {
    @NotNull(message = "name can not be null")
    @Size(min = 3, max = 20, message = "invalid size of name")
    private String firstName;
    @NotNull(message = "second name can not be null")
    @Size(min = 3, max = 20, message = "invalid size of second name")
    private String lastName;
    @NotNull(message = "Password cannot be null")
    @Size(min = 3, max = 20, message = "invalid size of password")
    private String password;
    @NotNull(message = "Email cannot be null")
    @Email
    private String email;
}
