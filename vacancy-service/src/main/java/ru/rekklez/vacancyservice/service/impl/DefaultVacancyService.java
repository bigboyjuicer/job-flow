package ru.rekklez.vacancyservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.rekklez.vacancyservice.entity.Status;
import ru.rekklez.vacancyservice.entity.VacancyEntity;
import ru.rekklez.vacancyservice.repository.VacancyJpaRepository;
import ru.rekklez.vacancyservice.service.VacancyService;
import ru.rekklez.vacancyservice.specification.VacancySpecification;

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

}
