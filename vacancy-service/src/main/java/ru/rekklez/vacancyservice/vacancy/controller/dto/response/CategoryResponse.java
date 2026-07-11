package ru.rekklez.vacancyservice.vacancy.controller.dto.response;

public record CategoryResponse(
        Long id,
        String name,
        String slug
) {
}
