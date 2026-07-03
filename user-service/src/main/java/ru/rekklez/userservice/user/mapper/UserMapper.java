package ru.rekklez.userservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userEntityToUser(UserEntity user);

    UserEntity userToUserEntity(User userDTO);
}
