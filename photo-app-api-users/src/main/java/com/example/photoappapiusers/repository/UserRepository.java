package com.example.photoappapiusers.repository;

import com.example.photoappapiusers.model.data.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String userName);
}
