package ru.rekklez.vacancyservice.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.rekklez.vacancyservice.entity.Status;
import ru.rekklez.vacancyservice.entity.VacancyEntity;

public class VacancySpecification {

    public static Specification<VacancyEntity> byCity(String city) {
        return (root, query, criteriaBuilder) ->
        {
            if(city == null) return null;
            return criteriaBuilder.equal(root.get("city"), city);
        };
    }

    //TODO: Доделать по категории
    /*public static Specification<VacancyEntity> byCategory(String city) {

    }*/

    public static Specification<VacancyEntity> bySalaryMin(Integer salaryMin) {
        return (root, query, criteriaBuilder) ->
        {
            if(salaryMin == null) return null;
            return criteriaBuilder.greaterThanOrEqualTo(root.get("salaryFrom"), salaryMin);
        };
    }

    public static Specification<VacancyEntity> byExperienceMax(Integer experienceMax) {
        return (root, query, criteriaBuilder) -> {
            if(experienceMax == null) return null;
            return criteriaBuilder.lessThanOrEqualTo(root.get("experienceYears"), experienceMax);
        };
    }

    public static Specification<VacancyEntity> byStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if(status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
