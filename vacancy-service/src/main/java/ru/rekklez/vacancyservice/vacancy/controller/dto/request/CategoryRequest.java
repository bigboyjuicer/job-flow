package ru.rekklez.vacancyservice.vacancy.controller.dto.request;

public record CategoryRequest(
        Long id,
        String name,
        String slug
) {
}
