package com.pass.service.user;

import com.pass.repository.user.UserGroupMappingEntity;
import com.pass.repository.user.UserGroupMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupMappingService {

    private final UserGroupMappingRepository userGroupMappingRepository;

    public UserGroupMappingService(UserGroupMappingRepository userGroupMappingRepository) {
        this.userGroupMappingRepository = userGroupMappingRepository;
    }

    public List<String> getAllUserGroupIds() {

        // user group id를 중복없이 user group id 역순으로 조회
        return userGroupMappingRepository.findDistinctUserGroupId();

    }
}