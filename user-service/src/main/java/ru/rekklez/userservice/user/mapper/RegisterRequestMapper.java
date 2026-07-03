package ru.rekklez.userservice.user.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.user.dto.RegisterRequest;
import ru.rekklez.userservice.user.entity.UserEntity;

@Mapper
public interface RegisterRequestMapper {

    RegisterRequestMapper INSTANCE = Mappers.getMapper(RegisterRequestMapper.class);

    @Mapping(source = "password", target = "passwordHash")
    UserEntity registerRequestToUser(RegisterRequest newUser);

}
