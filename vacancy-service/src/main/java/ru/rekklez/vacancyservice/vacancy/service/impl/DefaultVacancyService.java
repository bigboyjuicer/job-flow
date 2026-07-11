package ru.rekklez.vacancyservice.vacancy.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.UpdateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CreateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.entity.Status;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;
import ru.rekklez.vacancyservice.vacancy.mapper.VacancyMapper;
import ru.rekklez.vacancyservice.vacancy.repository.VacancyJpaRepository;
import ru.rekklez.vacancyservice.vacancy.service.VacancyService;
import ru.rekklez.vacancyservice.vacancy.specification.VacancySpecification;
import ru.rekklez.vacancyservice.web.exception.VacancyNotFoundException;

import java.util.List;

@Service
public class DefaultVacancyService implements VacancyService {

    private final VacancyJpaRepository repository;

    public DefaultVacancyService(VacancyJpaRepository repository) {
        this.repository = repository;
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
        VacancyEntity vacancy = VacancyMapper.INSTANCE.toVacancyEntity(vacancyToCreate);
        vacancy.setEmployerId(employerId);
        return repository.save(vacancy);
    }

    @Override
    @Transactional
    public VacancyEntity updateVacancy(Long id, UpdateVacancyRequest vacancyToUpdate, Long employerId) {
        if(!validEmployerId(id, employerId)) throw new IllegalArgumentException("You are not able to update this vacancy");

        VacancyEntity vacancy = VacancyMapper.INSTANCE.toVacancyEntity(vacancyToUpdate);
        vacancy.setId(id);
        vacancy.setEmployerId(employerId);

        return repository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id, Long employerId) {
        if(validEmployerId(id, employerId)) throw new IllegalArgumentException("You are not able to update this vacancy");
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
