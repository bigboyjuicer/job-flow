package ru.rekklez.vacancyservice.vacancy.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CategoryRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.UpdateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CreateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.entity.Status;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;
import ru.rekklez.vacancyservice.vacancy.mapper.CategoryMapper;
import ru.rekklez.vacancyservice.vacancy.mapper.VacancyMapper;
import ru.rekklez.vacancyservice.vacancy.repository.VacancyJpaRepository;
import ru.rekklez.vacancyservice.vacancy.service.VacancyService;
import ru.rekklez.vacancyservice.vacancy.specification.VacancySpecification;
import ru.rekklez.vacancyservice.web.exception.VacancyNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DefaultVacancyService implements VacancyService {

    private final VacancyJpaRepository repository;
    private final VacancyMapper vacancyMapper;
    private final CategoryMapper categoryMapper;

    public DefaultVacancyService(VacancyJpaRepository repository, VacancyMapper vacancyMapper, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.vacancyMapper = vacancyMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Page<VacancyEntity> getVacancies(Pageable page, String city, Integer salaryMin, Integer experienceMax, Status status) {
        Specification<VacancyEntity> spec = Specification.allOf(
                VacancySpecification.byCity(city),
                VacancySpecification.bySalaryMin(salaryMin),
                VacancySpecification.byExperienceMax(experienceMax),
                VacancySpecification.byStatus(status)
        );
        return repository.findAll(spec, page);
    }

    @Override
    public VacancyEntity getVacancy(Long id) {
        VacancyEntity vacancy = repository.findById(id)
                .orElseThrow(() -> new VacancyNotFoundException("Vacancy not found: id: " + id));
        return vacancy;
    }

    @Override
    @Transactional
    public VacancyEntity createVacancy(CreateVacancyRequest vacancyToCreate, Long employerId) {
        VacancyEntity vacancy = vacancyMapper.toVacancyEntity(vacancyToCreate);
        vacancy.setEmployerId(employerId);
        return repository.save(vacancy);
    }

    @Override
    @Transactional
    public VacancyEntity updateVacancy(Long id, UpdateVacancyRequest vacancyToUpdate, Long employerId) {
        VacancyEntity vacancy = repository.findById(id).orElseThrow(() -> new VacancyNotFoundException("Vacancy not found: id " + id));
        if(!vacancy.getEmployerId().equals(employerId)) throw new IllegalArgumentException("You are not able to update this vacancy");

        LocalDate createdAt = vacancy.getCreatedAt();

        vacancy = vacancyMapper.toVacancyEntity(vacancyToUpdate);

        vacancy.setId(id);
        vacancy.setEmployerId(employerId);
        vacancy.setCreatedAt(createdAt);

        //TODO: Разобраться с обновлением категорий

        return repository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id, Long employerId) {
        if (!validEmployerId(id, employerId))
            throw new IllegalArgumentException("You are not able to update this vacancy");
        repository.deleteVacancy(id);
    }

    @Override
    public List<VacancyEntity> getMyVacancies(Long employerId) {
        return repository.getVacancyByEmployerId(employerId);
    }

    private boolean validEmployerId(Long id, Long employerId) {
        VacancyEntity vacancy = repository.findById(id).orElseThrow(() -> new VacancyNotFoundException("Vacancy not found: id " + id));
        return vacancy.getEmployerId().equals(employerId);
    }
}
