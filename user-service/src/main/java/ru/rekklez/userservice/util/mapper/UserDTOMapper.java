package ru.rekklez.userservice.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.util.dto.UserDTO;

@Mapper
public interface UserDTOMapper {

    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    UserDTO userToUserDT0(User user);

    User userDTOToUser(UserDTO userDTO);
}
