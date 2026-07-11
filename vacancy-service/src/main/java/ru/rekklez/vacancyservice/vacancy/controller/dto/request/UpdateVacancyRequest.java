package ru.rekklez.vacancyservice.vacancy.controller.dto.request;

import ru.rekklez.vacancyservice.vacancy.entity.Status;

public record UpdateVacancyRequest(
        String title,
        String description,
        Integer salaryFrom,
        Integer salaryTo,
        String city,
        Integer experienceYears,
        Status status
) {
}
