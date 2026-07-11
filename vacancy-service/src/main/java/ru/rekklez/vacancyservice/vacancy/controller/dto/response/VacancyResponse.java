package ru.rekklez.vacancyservice.vacancy.controller.dto.response;

import ru.rekklez.vacancyservice.vacancy.entity.Status;

import java.time.LocalDate;
import java.util.List;

public record VacancyResponse(
        Long id,
        Long employerId,
        String title,
        String description,
        Integer salaryFrom,
        Integer salaryTo,
        String city,
        Integer experienceYears,
        Status status,
        LocalDate createdAt,
        LocalDate updatedAt,
        List<CategoryResponse> categories
) {}
