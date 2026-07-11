package ru.rekklez.vacancyservice.vacancy.service;

import org.springframework.data.domain.Page;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.UpdateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CreateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.entity.Status;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;
import org.springframework.data.domain.Pageable;

import java.nio.channels.FileChannel;
import java.util.List;

public interface VacancyService {

    Page<VacancyEntity> getVacancies(Pageable page, String city, Integer salaryMin, Integer experienceMax, Status status);

    VacancyEntity getVacancy(Long id);

    VacancyEntity createVacancy(CreateVacancyRequest vacancyToCreate, Long employerId);

    VacancyEntity updateVacancy(Long id, UpdateVacancyRequest vacancyToUpdate, Long employerId);

    void deleteVacancy(Long id, Long employerId);

    List<VacancyEntity> getMyVacancies(Long employerId);
}
