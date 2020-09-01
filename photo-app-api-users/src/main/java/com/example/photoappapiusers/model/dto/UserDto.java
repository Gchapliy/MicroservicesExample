package com.example.photoappapiusers.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    private static final long serialVersionUID = 3619120357225877358L;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String encryptedPassword;
}
