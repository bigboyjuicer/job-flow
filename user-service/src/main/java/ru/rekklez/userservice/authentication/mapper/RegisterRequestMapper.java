package ru.rekklez.userservice.authentication.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.authentication.controller.dto.request.RegisterRequest;
import ru.rekklez.userservice.user.entity.UserEntity;

@Mapper
public interface RegisterRequestMapper {

    RegisterRequestMapper INSTANCE = Mappers.getMapper(RegisterRequestMapper.class);

    @Mapping(source = "password", target = "passwordHash")
    UserEntity mapToUserEntity(RegisterRequest newUser);

}
