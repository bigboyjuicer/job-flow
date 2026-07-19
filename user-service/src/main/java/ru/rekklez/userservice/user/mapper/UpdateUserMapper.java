package ru.rekklez.userservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.user.controller.dto.request.UpdateUserProfileRequest;
import ru.rekklez.userservice.user.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {

    UserEntity mapToUserEntity(UpdateUserProfileRequest updateUserProfileRequest);

}
