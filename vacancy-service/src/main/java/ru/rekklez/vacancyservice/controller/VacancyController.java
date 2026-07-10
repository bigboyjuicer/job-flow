package ru.rekklez.vacancyservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.ApiResponse;
import ru.rekklez.vacancyservice.dto.response.VacancyResponse;
import ru.rekklez.vacancyservice.entity.Status;
import ru.rekklez.vacancyservice.mapper.VacancyMapper;
import ru.rekklez.vacancyservice.service.VacancyService;

import java.util.List;


@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService service;

    public VacancyController(VacancyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VacancyResponse>>> getVacancies(
            @RequestParam Integer page,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) Integer experienceMax,
            @RequestParam(required = false) Status status
    ) {
        List<VacancyResponse> vacancies =
                service
                        .getVacancies(Pageable.ofSize(50).withPage(page), city, salaryMin, experienceMax, status)
                        .map(VacancyMapper.INSTANCE::toVacancyResponse)
                        .getContent();
        return ResponseEntity.ok().body(ApiResponse.success(vacancies, "Successfully found vacancies"));
    }

}
