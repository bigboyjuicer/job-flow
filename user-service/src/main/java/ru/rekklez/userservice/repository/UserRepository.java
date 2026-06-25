package ru.rekklez.userservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.rekklez.userservice.entity.User;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, BigInteger> {

    @Query("SELECT u FROM User u WHERE u.email = :email and u.passwordHash = :password")
    Optional<User> findUser(String email, String password);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);

}
