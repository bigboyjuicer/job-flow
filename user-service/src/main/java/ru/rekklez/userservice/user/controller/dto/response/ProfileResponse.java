package ru.rekklez.userservice.user.controller.dto.response;

public record ProfileResponse(
        String email,
        String firstName,
        String lastName,
        String companyName
) {
}
