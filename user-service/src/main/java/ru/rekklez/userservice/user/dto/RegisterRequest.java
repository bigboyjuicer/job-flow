package ru.rekklez.userservice.user.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import ru.rekklez.userservice.user.entity.Role;

public class RegisterRequest {

        @NotNull(message = "Почта не должна быть пустой")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" , message = "Неверный формат почты")
        private String email;

        @NotNull(message = "Пароль не должен быть null")
        @Length(min = 8, max = 255, message = "Пароль должен быть длиной от 8 до 255 символов")
        private String password;

        @NotNull(message = "Роль не должна быть null")
        private Role role;

        @NotNull(message = "Имя не должно быть null")
        @Length(min = 2, max = 255, message = "Имя должно быть длиной от 2 до 255 символов")
        private String firstName;

        @NotNull(message = "Фамилия не должна быть null")
        @NotBlank(message = "Поле не должно быть пустым")
        @Length(max = 255, message = "Фамилия должна быть длиной до 255 символов")
        private String lastName;

        @Length(min = 1, max = 255, message = "Название компании должно быть длиной от 1 до 255 символов")
        private String companyName;

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

        public Role getRole() {
                return role;
        }

        public String getFirstName() {
                return firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public String getCompanyName() {
                return companyName;
        }

}
