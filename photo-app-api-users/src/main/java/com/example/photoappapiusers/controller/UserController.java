package com.example.photoappapiusers.controller;

import com.example.photoappapiusers.model.User;
import com.example.photoappapiusers.model.UserResponseModel;
import com.example.photoappapiusers.model.dto.UserDto;
import com.example.photoappapiusers.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port") + "" +
                ", with token = " + environment.getProperty("token.secret");
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody User user) {

        UserDto userDto = userService.createUser(modelMapper.map(user, UserDto.class));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(userDto, UserResponseModel.class));
    }

    //@PreAuthorize("principal == #userId")
    @PostAuthorize("principal == returnObject.getBody().getUserId()")
    @GetMapping(value = "/{userId}", produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId")String userId){

        UserDto userDto = userService.getUserByUserId(userId);
        UserResponseModel returnValue = modelMapper.map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
