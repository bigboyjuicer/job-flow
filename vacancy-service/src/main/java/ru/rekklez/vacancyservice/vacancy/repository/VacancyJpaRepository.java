package ru.rekklez.vacancyservice.vacancy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;

import java.util.List;

@Repository
public interface VacancyJpaRepository extends JpaRepository<VacancyEntity, Long>, JpaSpecificationExecutor<VacancyEntity> {

    @Modifying
    @Query("UPDATE VacancyEntity v SET v.status = Status.CLOSED WHERE v.id = :id")
    void deleteVacancy(Long id);

    @Query("SELECT v FROM VacancyEntity v WHERE v.employerId = :employerId")
    List<VacancyEntity> getVacancyByEmployerId(Long employerId);
}
