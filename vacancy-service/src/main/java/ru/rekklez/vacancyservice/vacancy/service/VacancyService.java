package ru.rekklez.vacancyservice.vacancy.service;

import org.springframework.data.domain.Page;
import ru.rekklez.vacancyservice.vacancy.entity.Status;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;
import org.springframework.data.domain.Pageable;

public interface VacancyService {

    Page<VacancyEntity> getVacancies(Pageable page, String city, Integer salaryMin, Integer experienceMax, Status status);
}
