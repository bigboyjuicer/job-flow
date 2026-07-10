package ru.rekklez.vacancyservice.service;

import org.springframework.data.domain.Page;
import ru.rekklez.vacancyservice.entity.Status;
import ru.rekklez.vacancyservice.entity.VacancyEntity;
import org.springframework.data.domain.Pageable;

public interface VacancyService {

    Page<VacancyEntity> getVacancies(Pageable page, String city, Integer salaryMin, Integer experienceMax, Status status);
}
