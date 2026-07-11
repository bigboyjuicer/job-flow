package ru.rekklez.vacancyservice.vacancy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.vacancyservice.vacancy.controller.dto.response.VacancyResponse;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;

@Mapper
public interface VacancyMapper {

    VacancyMapper INSTANCE = Mappers.getMapper(VacancyMapper.class);

    VacancyResponse toVacancyResponse(VacancyEntity vacancy);

}
