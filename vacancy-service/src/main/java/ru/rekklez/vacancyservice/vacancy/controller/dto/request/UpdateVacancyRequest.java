package ru.rekklez.vacancyservice.vacancy.controller.dto.request;

import ru.rekklez.vacancyservice.vacancy.entity.Status;

import java.util.List;

public record UpdateVacancyRequest(
        String title,
        String description,
        Integer salaryFrom,
        Integer salaryTo,
        String city,
        Integer experienceYears,
        Status status,
        List<CategoryRequest> categories
) {
}
