package com.pass.service.user;

import com.pass.repository.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserModelMapper {

    UserModelMapper INSTANCE = Mappers.getMapper(UserModelMapper.class);

    User toUser(UserEntity userEntity);

}