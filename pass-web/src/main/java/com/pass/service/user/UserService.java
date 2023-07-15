package com.pass.service.user;

import com.pass.repository.user.UserEntity;
import com.pass.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUser(String userId) {

        // userId를 조건으로 사용자 정보 조회. 프로필에 노출 할 사용자 이름 필요
        UserEntity userEntity = userRepository.findByUserId(userId);

        return UserModelMapper.INSTANCE.toUser(userEntity);

    }
}
