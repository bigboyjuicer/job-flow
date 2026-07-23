package ru.rekklez.userservice.user.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.entity.Role;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.repository.UserRepository;
import ru.rekklez.userservice.web.exception.UserAlreadyExistsException;
import ru.rekklez.userservice.web.exception.WrongPasswordException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DefaultUserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity(
                "1234@mail.ru",
                "12345678",
                Role.CANDIDATE,
                "Ivan",
                "Petrov",
                null
        );
    }

    @Nested
    @DisplayName("Подгрузка пользователя по почте")
    class LoadByEmail {

        @Test
        @DisplayName("Удачная загрузка пользователя по Email")
        void shouldLoadUserByEmail() {
            SecurityUser securityUser = new SecurityUser(user);

            when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

            assertEquals(securityUser, userService.loadUserByUsername(user.getEmail()));

            verify(userRepository).findUserByEmail(user.getEmail());
        }

        @Test
        @DisplayName("Загрузка пользователя с несуществующим Email")
        void shouldThrowUsernameNotFound() {
            when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));

            verify(userRepository).findUserByEmail(user.getEmail());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Загрузка пользователя с null или пустым email")
        void shouldThrowIllegalArgumentExceptionWithNullEmail(String email) {
            assertThrows(IllegalArgumentException.class, () -> userService.loadUserByUsername(email));

            verifyNoInteractions(userRepository);
        }

    }

    @Nested
    @DisplayName("Подгрузка пользователя по ID")
    class LoadById {

        @Test
        @DisplayName("Удачная загрузка пользователя по ID")
        void shouldLoadUserById() {
            Long id = 5L;

            user.setId(id);

            when(userRepository.findById(id)).thenReturn(Optional.of(user));

            assertEquals(user, userService.loadUserById(id));

            verify(userRepository).findById(id);
        }

        @Test
        @DisplayName("Загрузка пользователя с несуществующим ID")
        void shouldThrowUsernameNotFound() {
            long id = 5L;

            when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserById(id));

            verify(userRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("Создание нового пользователя")
    class CreateUser {

        @Test
        @DisplayName("Удачная регистрация пользователя")
        void shouldCreateUser() {
            UserEntity newUser = new UserEntity(
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getRole(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCompanyName()
            );

            String rawPassword = newUser.getPasswordHash();
            String encodedPassword = "ENCODED_" + rawPassword;

            when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
            when(passwordEncoder.encode(newUser.getPasswordHash())).thenReturn(encodedPassword);
            when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            UserEntity savedUser = userService.createUser(newUser);

            assertAll(
                    () -> assertEquals(encodedPassword, savedUser.getPasswordHash()),
                    () -> assertNotEquals(rawPassword, savedUser.getPasswordHash())
            );

            verify(userRepository).existsByEmail(newUser.getEmail());
            verify(passwordEncoder).encode(rawPassword);
            verify(userRepository).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("Регистрация пользователя уже с существующим email в базе")
        void shouldThrowUserAlreadyExists() {
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(user));

            verify(userRepository).existsByEmail(user.getEmail());
            verifyNoMoreInteractions(userRepository, passwordEncoder);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Регистрация пользователя с null user")
        void shouldThrowIllegalArgumentException(UserEntity user) {
            assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

            verifyNoInteractions(userRepository, passwordEncoder);
        }
    }

    @Nested
    @DisplayName("Обновление профиля пользователя")
    class UpdateUserProfile {

        @Test
        @DisplayName("Удачное обновление профиля пользователя")
        void updateUserSuccessfulTest() {
            when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
            when(userRepository.updateUserProfile(user.getEmail(), user.getFirstName(), user.getLastName(), user.getCompanyName()))
                    .thenReturn(1);

            UserEntity result = userService.updateUser(user);

            assertEquals(user, result);

            verify(userRepository).existsByEmail(user.getEmail());
            verify(userRepository).updateUserProfile(user.getEmail(), user.getFirstName(), user.getLastName(), user.getCompanyName());
        }

        @Test
        @DisplayName("Обновление профиля пользователя при несуществующем пользователе")
        void updateUserNotFoundTest() {
            when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

            assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(user));

            verify(userRepository).existsByEmail(user.getEmail());
            verifyNoMoreInteractions(userRepository);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Обновление профиля пользователя с null user")
        void shouldThrowIllegalArgumentException(UserEntity user) {
            assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));

            verifyNoInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Изменение пароля пользователя")
    class ChangePassword {

        @Test
        @DisplayName("Удачная смена пароля")
        void shouldUpdatePassword() {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

            String email = authentication.getName();
            String oldPassword = user.getPasswordHash();
            String newPassword = "123456789";
            String newEncodedPassword = "ENCODED_" + newPassword;

            when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(oldPassword, user.getPasswordHash())).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(newEncodedPassword);
            when(userRepository.updateUserPassword(user.getEmail(), newEncodedPassword)).thenReturn(1);

            userService.updatePassword(oldPassword, newPassword, authentication);

            verify(userRepository).findUserByEmail(email);
            verify(passwordEncoder).matches(oldPassword, user.getPasswordHash());
            verify(passwordEncoder).encode(newPassword);

            ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
            verify(userRepository).updateUserPassword(eq(user.getEmail()), passwordCaptor.capture());

            String encodedPassword = passwordCaptor.getValue();
            assertEquals(newEncodedPassword, encodedPassword);
        }

        @ParameterizedTest
        @CsvSource({
                "[null], [null]",
                "'',     ''",
                "[null], ''",
                "'',     [null]"
        })
        @DisplayName("Смена пароля с пустым или null паролем")
        void shouldThrowIllegalArgumentExceptionWithNullOrEmptyPasswords(String oldPassword, String newPassword) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

            assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(oldPassword, newPassword, authentication));

            verifyNoInteractions(userRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Смена пароля на тот же самый пароль")
        void shouldThrowIllegalArgumentExceptionWithEqualsPasswords() {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);
            String oldPassword = "12345678";
            String newPassword = "12345678";

            assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(oldPassword, newPassword, authentication));

            verifyNoInteractions(userRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Смена пароля при null аутентификации")
        void shouldThrowAuthenticationException() {
            String oldPassword = "12345678";
            String newPassword = "123456789";

            assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userService.updatePassword(oldPassword, newPassword, null));

            verifyNoInteractions(userRepository, passwordEncoder);
        }

        @Test
        @DisplayName("Смена пароля с неверным паролем пользователя")
        void shouldThrowWrongPasswordException() {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

            String email = authentication.getName();
            String oldPassword = user.getPasswordHash();
            String newPassword = "123456789";

            when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(oldPassword, user.getPasswordHash())).thenReturn(false);

            assertThrows(WrongPasswordException.class, () -> userService.updatePassword(oldPassword, newPassword, authentication));

            verify(userRepository).findUserByEmail(email);
            verify(passwordEncoder).matches(oldPassword, user.getPasswordHash());
            verifyNoMoreInteractions(userRepository, passwordEncoder);
        }
    }
}
