package ru.rekklez.userservice.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rekklez.userservice.user.entity.UserEntity;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, BigInteger> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email and u.passwordHash = :password")
    Optional<UserEntity> findUser(String email, String password);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserByEmail(String email);

}
