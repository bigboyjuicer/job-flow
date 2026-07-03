package ru.rekklez.userservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class LoginRequest {

    @NotNull(message = "Почта не должна быть null")
    @Email(message = "Неверный формат почты")
    private String email;

    @NotNull(message = "Пароль не должен быть null")
    @Length(min = 8, max = 255, message = "Пароль должен быть длиной не меньше 8 и не больше 255 символов")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
