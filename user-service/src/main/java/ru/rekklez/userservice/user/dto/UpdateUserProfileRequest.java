package ru.rekklez.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UpdateUserProfileRequest(

        @NotNull(message = "Имя не должно быть null")
        @Length(min = 2, max = 255, message = "Имя должно быть длиной от 2 до 255 символов")
        String firstName,

        @NotNull(message = "Фамилия не должна быть null")
        @NotBlank(message = "Поле не должно быть пустым")
        @Length(max = 255, message = "Фамилия должна быть длиной до 255 символов")
        String lastName,

        @Length(min = 1, max = 255, message = "Название компании должно быть длиной от 1 до 255 символов")
        String companyName
) {
}
