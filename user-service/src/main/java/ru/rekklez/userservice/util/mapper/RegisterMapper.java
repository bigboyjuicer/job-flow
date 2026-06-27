package ru.rekklez.userservice.util.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.util.dto.RegisterRequest;

@Mapper
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    @Mapping(source = "passwordHash", target = "password")
    RegisterRequest userToRegisterDTO(User user);

    @Mapping(source = "password", target = "passwordHash")
    User registerDTOToUser(RegisterRequest newUser);

}
