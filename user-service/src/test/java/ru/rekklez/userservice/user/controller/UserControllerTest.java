package ru.rekklez.userservice.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rekklez.userservice.authentication.service.TokenService;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.controller.dto.request.UpdatePasswordRequest;
import ru.rekklez.userservice.user.controller.dto.request.UpdateUserProfileRequest;
import ru.rekklez.userservice.user.controller.dto.response.ProfileResponse;
import ru.rekklez.userservice.user.entity.Role;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.mapper.ProfileResponseMapper;
import ru.rekklez.userservice.user.mapper.UpdateUserMapper;
import ru.rekklez.userservice.user.service.UserService;
import ru.rekklez.userservice.web.exception.WrongPasswordException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProfileResponseMapper profileResponseMapper;

    @MockitoBean
    private UpdateUserMapper updateUserMapper;

    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(
                "1234@mail.ru", null, List.of(new SimpleGrantedAuthority("ROLE_CANDIDATE")));
    }

    @Nested
    @DisplayName("Получение профиля пользователя")
    class GetProfile {

        @Test
        @DisplayName("Удачное получение профиля пользователя")
        void shouldReturnProfile() throws Exception {
            UserEntity userEntity = new UserEntity("1234@mail.ru", "encoded_password", Role.CANDIDATE, "Ivan", "Petrov", null);
            ProfileResponse profileResponse = new ProfileResponse("1234@mail.ru", "Ivan", "Petrov", null);

            when(userService.loadUserByUsername("1234@mail.ru")).thenReturn(new SecurityUser(userEntity));
            when(profileResponseMapper.mapToProfileResponse(userEntity)).thenReturn(profileResponse);

            mockMvc.perform(get("/users/me").principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Successfully loaded user profile"))
                    .andExpect(jsonPath("$.data.email").value("1234@mail.ru"))
                    .andExpect(jsonPath("$.data.firstName").value("Ivan"))
                    .andExpect(jsonPath("$.data.lastName").value("Petrov"))
                    .andExpect(jsonPath("$.data.companyName").doesNotExist());

            verify(userService).loadUserByUsername("1234@mail.ru");
            verify(profileResponseMapper).mapToProfileResponse(userEntity);
        }

        @Test
        @DisplayName("Получение профиля если профиля нет в базе данных")
        void shouldReturnNotFoundStatus() throws Exception {
            when(userService.loadUserByUsername(authentication.getName())).thenThrow(UsernameNotFoundException.class);

            mockMvc.perform(get("/users/me").principal(authentication))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User not found"));

            verify(userService).loadUserByUsername(authentication.getName());
            verifyNoInteractions(profileResponseMapper);
        }

    }

    @Nested
    @DisplayName("Обновление профиля пользователя")
    class PutProfile {

        @Test
        @DisplayName("Удачное изменение профиля пользователя")
        void shouldUpdateProfile() throws Exception {
            UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                    "newFirstName",
                    "newLastName",
                    "newCompany"
            );
            UserEntity updateEntity = new UserEntity(authentication.getName(), null, null, updateRequest.firstName(), updateRequest.lastName(), updateRequest.companyName());
            ProfileResponse profile = new ProfileResponse(updateEntity.getEmail(), updateEntity.getFirstName(), updateEntity.getLastName(), updateEntity.getCompanyName());
            when(updateUserMapper.mapToUserEntity(updateRequest)).thenReturn(updateEntity);
            when(userService.updateUser(updateEntity)).thenReturn(updateEntity);
            when(profileResponseMapper.mapToProfileResponse(updateEntity)).thenReturn(profile);

            mockMvc.perform(put("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateRequest))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Successfully updated profile"))
                    .andExpect(jsonPath("$.data.email").value("1234@mail.ru"))
                    .andExpect(jsonPath("$.data.firstName").value("newFirstName"))
                    .andExpect(jsonPath("$.data.lastName").value("newLastName"))
                    .andExpect(jsonPath("$.data.companyName").value("newCompany"));

            verify(updateUserMapper).mapToUserEntity(updateRequest);
            verify(userService).updateUser(updateEntity);
            verify(profileResponseMapper).mapToProfileResponse(updateEntity);
        }

        @Test
        @DisplayName("Изменение профиля с несуществующим пользователем")
        void shouldReturnNotFoundWithUserNotFound() throws Exception {
            UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                    "newFirstName",
                    "newLastName",
                    "newCompany"
            );
            UserEntity updateEntity = new UserEntity(authentication.getName(), null, null, updateRequest.firstName(), updateRequest.lastName(), updateRequest.companyName());
            when(updateUserMapper.mapToUserEntity(updateRequest)).thenReturn(updateEntity);
            when(userService.updateUser(updateEntity)).thenThrow(UsernameNotFoundException.class);

            mockMvc.perform(put("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateRequest))
                            .principal(authentication))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User not found"));

            verify(updateUserMapper).mapToUserEntity(any(UpdateUserProfileRequest.class));
            verify(userService).updateUser(any(UserEntity.class));
            verifyNoInteractions(profileResponseMapper);
        }

        @Test
        @DisplayName("Изменение профиля с невалидными полями")
        void shouldReturnBadRequestWithValidError() throws Exception {
            UpdateUserProfileRequest updateRequest = new UpdateUserProfileRequest(
                    "n",
                    null,
                    ""
            );

            mockMvc.perform(put("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateRequest))
                            .principal(authentication))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("You have promoted invalid data"))
                    .andExpect(jsonPath("$.errors.firstName").value("Имя должно быть длиной от 2 до 255 символов"))
                    .andExpect(jsonPath("$.errors.lastName").value("Поле не должно быть пустым"))
                    .andExpect(jsonPath("$.errors.companyName").value("Название компании должно быть длиной от 1 до 255 символов"));


            verifyNoInteractions(updateUserMapper, userService, profileResponseMapper);
        }
    }

    @Nested
    @DisplayName("Изменение пароля")
    class UpdatePassword {

        @Test
        @DisplayName("Удачное изменение пароля")
        void shouldChangePassword() throws Exception {
            UpdatePasswordRequest updatePassword = new UpdatePasswordRequest("12345678", "123456789");

            mockMvc.perform(put("/users/me/password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updatePassword))
                    .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Successfully changed password"));

            verify(userService).updatePassword(updatePassword.oldPassword(), updatePassword.newPassword(), authentication);
        }

        @Test
        @DisplayName("Изменение пароля с неверным паролем от аккаунта")
        void shouldReturnBadRequestWithWrongPassword() throws Exception {
            UpdatePasswordRequest updatePassword = new UpdatePasswordRequest("12345678", "123456789");

            doThrow(new WrongPasswordException("Wrong password")).when(userService).updatePassword(updatePassword.oldPassword(), updatePassword.newPassword(), authentication);

            mockMvc.perform(put("/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatePassword))
                            .principal(authentication))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Wrong password"));

            verify(userService).updatePassword(updatePassword.oldPassword(), updatePassword.newPassword(), authentication);
        }

        @Test
        @DisplayName("Изменение пароля с невалидными данными")
        void shouldReturnBadRequestWithValidError() throws Exception {
            UpdatePasswordRequest updatePassword = new UpdatePasswordRequest(null, "12345");

            mockMvc.perform(put("/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatePassword))
                            .principal(authentication))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("You have promoted invalid data"))
                    .andExpect(jsonPath("$.errors.oldPassword").value("Пароль не должен быть null"))
                    .andExpect(jsonPath("$.errors.newPassword").value("Пароль должен быть длиной от 8 до 255 символов"));

            verifyNoInteractions(userService);
        }
    }

    @Nested
    @DisplayName("Получение профиля кандидата по ID")
    class GetCandidateById {

        @Test
        @DisplayName("Удачное получение профиля кандидата")
        void shouldGetCandidate() throws Exception {
            long id = 15L;
            UserEntity user = new UserEntity("1234@mail.ru", "encoded_password", Role.CANDIDATE, "Ivan", "Petrov", null);
            ProfileResponse profile = new ProfileResponse(user.getEmail(), user.getFirstName(), user.getLastName(), user.getCompanyName());

            when(userService.loadUserById(id)).thenReturn(user);
            when(profileResponseMapper.mapToProfileResponse(user)).thenReturn(profile);

            mockMvc.perform(get("/users/{id}", id)
                    .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Successfully loaded candidate profile"))
                    .andExpect(jsonPath("$.data.email").value("1234@mail.ru"))
                    .andExpect(jsonPath("$.data.firstName").value("Ivan"))
                    .andExpect(jsonPath("$.data.lastName").value("Petrov"))
                    .andExpect(jsonPath("$.data.companyName").doesNotExist());

            verify(userService).loadUserById(id);
            verify(profileResponseMapper).mapToProfileResponse(user);
        }

        @Test
        @DisplayName("Получение профиля кандидата при несуществующем аккаунте")
        void shouldReturnNotFoundWithUserNotFound() throws Exception {
            long id = 15L;
            UserEntity user = new UserEntity("1234@mail.ru", "encoded_password", Role.CANDIDATE, "Ivan", "Petrov", null);

            when(userService.loadUserById(id)).thenThrow(UsernameNotFoundException.class);

            mockMvc.perform(get("/users/{id}", id)
                            .principal(authentication))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("User not found"));

            verify(userService).loadUserById(id);
            verifyNoInteractions(profileResponseMapper);
        }

        @Test
        @DisplayName("Получение профиля кандидата с неверным типо передаваемого ID")
        void shouldReturnBadRequestWithWrongIdType() throws Exception {
            String id = "5L";

            mockMvc.perform(get("/users/{id}", id)
                            .principal(authentication))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Method argument mismatch: id"));
        }

    }

    @ParameterizedTest
    @ValueSource(
          strings =
                  {"/users/me", "/users/me/password"}
    )
    @DisplayName("Вызов эндпоинтов с null телом запроса")
    void shouldReturnBadRequestWithNullRequestBody(String endpoint) throws Exception {
        mockMvc.perform(put(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or incorrect"));

        verifyNoInteractions(updateUserMapper, userService, profileResponseMapper);
    }
}
