package com.example.photoappapiusers.service;

import com.example.photoappapiusers.model.data.UserEntity;
import com.example.photoappapiusers.model.dto.UserDto;
import com.example.photoappapiusers.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    ModelMapper modelMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserEntity userEntity = userRepository.findByEmail(userName);

        if(userEntity == null) throw new UsernameNotFoundException(userName);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        return modelMapper.map(userEntity, UserDto.class);
    }
}
