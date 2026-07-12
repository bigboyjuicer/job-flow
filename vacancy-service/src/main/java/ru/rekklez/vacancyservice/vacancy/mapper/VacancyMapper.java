package ru.rekklez.vacancyservice.vacancy.mapper;

import org.mapstruct.*;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.UpdateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CreateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.response.VacancyResponse;
import ru.rekklez.vacancyservice.vacancy.entity.VacancyEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {VacancyMapper.class, CategoryMapper.class}
)
public interface VacancyMapper {

    VacancyResponse toVacancyResponse(VacancyEntity vacancy);

    VacancyEntity toVacancyEntity(CreateVacancyRequest createVacancyRequest);

    VacancyEntity toVacancyEntity(UpdateVacancyRequest updateVacancyRequest);

}
