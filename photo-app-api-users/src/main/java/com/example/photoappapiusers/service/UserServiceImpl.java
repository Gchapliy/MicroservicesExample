package com.example.photoappapiusers.service;

import com.example.photoappapiusers.model.AlbumResponseModel;
import com.example.photoappapiusers.model.data.AlbumServiceClient;
import com.example.photoappapiusers.model.data.UserEntity;
import com.example.photoappapiusers.model.dto.UserDto;
import com.example.photoappapiusers.repository.UserRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    ModelMapper modelMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    //RestTemplate restTemplate;
    AlbumServiceClient albumServiceClient;
    Environment env;

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

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);

//        String albumsUrl = String.format(env.getProperty("albums.url"), userId);
//
//        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//        });
//        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        List<AlbumResponseModel> albumsList = null;

        try {
            albumsList = albumServiceClient.getAlbums(userId);
        } catch (FeignException e){
            log.error(e.getLocalizedMessage());
        }

        userDto.setAlbums(albumsList);

        return userDto;
    }
}
