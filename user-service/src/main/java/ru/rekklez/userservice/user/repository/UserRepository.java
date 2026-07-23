package ru.rekklez.userservice.user.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rekklez.userservice.user.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.firstName = :firstName, u.lastName = :lastName, u.companyName = :companyName WHERE u.email = :email")
    int updateUserProfile(String email, String firstName, String lastName, String companyName);

    @Modifying
    @Query("UPDATE UserEntity u SET u.passwordHash = :password WHERE u.email = :email")
    int updateUserPassword(String email, String password);

    boolean existsByEmail(String email);

}
