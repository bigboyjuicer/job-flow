package ru.rekklez.userservice.authentication.controller.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import ru.rekklez.userservice.user.entity.Role;

public record RegisterRequest(

        @NotNull(message = "Почта не должна быть null")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" , message = "Неверный формат почты")
        String email,

        @NotNull(message = "Пароль не должен быть null")
        @Length(min = 8, max = 255, message = "Пароль должен быть длиной от 8 до 255 символов")
        String password,

        @NotNull(message = "Роль не должна быть null")
        Role role,

        @NotBlank(message = "Поле не должно быть пустым")
        @Length(min = 2, max = 255, message = "Имя должно быть длиной от 2 до 255 символов")
        String firstName,

        @NotBlank(message = "Поле не должно быть пустым")
        @Length(max = 255, message = "Фамилия должна быть длиной до 255 символов")
        String lastName,

        @Length(min = 1, max = 255, message = "Название компании должно быть длиной от 1 до 255 символов")
        String companyName

) {
}
