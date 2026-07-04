package ru.rekklez.userservice.user.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdatePasswordRequest(

        @NotNull(message = "Пароль не должен быть null")
        @Length(min = 8, max = 255, message = "Пароль должен быть длиной от 8 до 255 символов")
        String password

) {
}
