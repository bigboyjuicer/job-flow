package ru.rekklez.userservice.util.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.util.dto.RegisterDTO;

@Mapper
public interface RegisterDTOMapper {

    RegisterDTOMapper INSTANCE = Mappers.getMapper(RegisterDTOMapper.class);

    @Mapping(source = "passwordHash", target = "password")
    RegisterDTO userToRegisterDTO(User user);

    @Mapping(source = "password", target = "passwordHash")
    User registerDTOToUser(RegisterDTO newUser);

}
