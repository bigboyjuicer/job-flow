package ru.rekklez.userservice.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateUserProfileRequest(

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
