package ru.rekklez.vacancyservice.vacancy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CategoryRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.response.CategoryResponse;
import ru.rekklez.vacancyservice.vacancy.entity.CategoryEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(CategoryEntity categoryEntity);

    CategoryEntity toCategoryEntity(CategoryRequest categoryRequest);

}
