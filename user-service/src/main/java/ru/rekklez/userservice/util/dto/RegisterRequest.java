package ru.rekklez.userservice.util.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import ru.rekklez.userservice.util.enums.Role;

public class RegisterRequest {

    @NotNull(message = "Почта не должна быть null")
    @Email(message = "Неверный формат почты")
    private String email;

    @NotNull(message = "Пароль не должен быть null")
    @Length(min = 8, max = 255, message = "Пароль должен быть длиной не меньше 8 и не больше 255 символов")
    private String password;

    @NotNull(message = "Роль не должна быть null")
    private Role role;

    @NotNull(message = "Имя не должно быть null")
    @Length(min = 2, max = 8, message = "Имя должно быть длиной не меньше 2 и не больше 255 символов")
    private String firstName;

    @NotNull(message = "Фамилия не должна быть null")
    @NotBlank(message = "Поле не должно быть пустым")
    @Length(max = 255, message = "Фамилия не должна быть больше 255 символов")
    private String lastName;

    @NotBlank(message = "Поле не должно быть пустым")
    @Length(message = "Название компании не должно быть больше 255 символов")
    private String companyName;

    public RegisterRequest(String password, String email, Role role, String firstName, String lastName, String companyName) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }

    public @NotNull(message = "Почта не должна быть null") @Email(message = "Неверный формат почты") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Почта не должна быть null") @Email(message = "Неверный формат почты") String email) {
        this.email = email;
    }

    public @NotNull(message = "Пароль не должен быть null") @Length(min = 8, max = 255, message = "Пароль должен быть длиной не меньше 8 и не больше 255 символов") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Пароль не должен быть null") @Length(min = 8, max = 255, message = "Пароль должен быть длиной не меньше 8 и не больше 255 символов") String password) {
        this.password = password;
    }

    public @NotNull(message = "Роль не должна быть null") Role getRole() {
        return role;
    }

    public void setRole(@NotNull(message = "Роль не должна быть null") Role role) {
        this.role = role;
    }

    public @NotNull(message = "Имя не должно быть null") @Length(min = 2, max = 8, message = "Имя должно быть длиной не меньше 2 и не больше 255 символов") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull(message = "Имя не должно быть null") @Length(min = 2, max = 8, message = "Имя должно быть длиной не меньше 2 и не больше 255 символов") String firstName) {
        this.firstName = firstName;
    }

    public @NotNull(message = "Фамилия не должна быть null") @NotBlank(message = "Поле не должно быть пустым") @Length(max = 255, message = "Фамилия не должна быть больше 255 символов") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull(message = "Фамилия не должна быть null") @NotBlank(message = "Поле не должно быть пустым") @Length(max = 255, message = "Фамилия не должна быть больше 255 символов") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Поле не должно быть пустым") @Length(message = "Название компании не должно быть больше 255 символов") String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(@NotBlank(message = "Поле не должно быть пустым") @Length(message = "Название компании не должно быть больше 255 символов") String companyName) {
        this.companyName = companyName;
    }
}
