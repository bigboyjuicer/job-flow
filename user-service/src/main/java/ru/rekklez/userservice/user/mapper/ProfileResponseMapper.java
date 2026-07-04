package ru.rekklez.userservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.user.controller.dto.response.ProfileResponse;
import ru.rekklez.userservice.user.entity.UserEntity;

@Mapper
public interface ProfileResponseMapper {

    ProfileResponseMapper INSTANCE = Mappers.getMapper(ProfileResponseMapper.class);

    ProfileResponse mapToProfileResponse(UserEntity user);

}
