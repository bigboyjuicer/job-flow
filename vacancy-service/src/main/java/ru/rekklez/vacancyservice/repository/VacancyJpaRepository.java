package ru.rekklez.vacancyservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.rekklez.vacancyservice.entity.VacancyEntity;

@Repository
public interface VacancyJpaRepository extends JpaRepository<VacancyEntity, Long>, JpaSpecificationExecutor<VacancyEntity> {
}
