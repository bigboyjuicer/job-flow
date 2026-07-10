package ru.rekklez.vacancyservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rekklez.vacancyservice.dto.response.VacancyResponse;
import ru.rekklez.vacancyservice.entity.VacancyEntity;

@Mapper
public interface VacancyMapper {

    VacancyMapper INSTANCE = Mappers.getMapper(VacancyMapper.class);

    VacancyResponse toVacancyResponse(VacancyEntity vacancy);

}
