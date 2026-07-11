package ru.rekklez.vacancyservice.vacancy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.ApiResponse;
import ru.rekklez.vacancyservice.security.SecurityUser;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.UpdateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.request.CreateVacancyRequest;
import ru.rekklez.vacancyservice.vacancy.controller.dto.response.VacancyResponse;
import ru.rekklez.vacancyservice.vacancy.entity.Status;
import ru.rekklez.vacancyservice.vacancy.mapper.VacancyMapper;
import ru.rekklez.vacancyservice.vacancy.service.VacancyService;

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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VacancyResponse>> getVacancy(@PathVariable Long id) {
        VacancyResponse vacancy = VacancyMapper.INSTANCE.toVacancyResponse(service.getVacancy(id));
        return ResponseEntity.ok().body(ApiResponse.success(vacancy, "Vacancy successfully found"));
    }

    @PostMapping
    @Secured("ROLE_EMPLOYER")
    public ResponseEntity<ApiResponse<VacancyResponse>> createVacancy(CreateVacancyRequest vacancyToCreate, Authentication authentication) {
        Long employerId = getEmployerId(authentication);
        VacancyResponse vacancy = VacancyMapper.INSTANCE.toVacancyResponse(service.createVacancy(vacancyToCreate, employerId));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(vacancy, "Vacancy successfully created"));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_EMPLOYER")
    public ResponseEntity<ApiResponse<VacancyResponse>> updateVacancy(@PathVariable Long id, UpdateVacancyRequest vacancyToUpdate, Authentication authentication) {
        Long employerId = getEmployerId(authentication);
        VacancyResponse vacancy = VacancyMapper.INSTANCE.toVacancyResponse(service.updateVacancy(id, vacancyToUpdate, employerId));
        return ResponseEntity.ok().body(ApiResponse.success(vacancy, "Vacancy successfully updated"));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_EMPLOYER")
    public ResponseEntity<ApiResponse<Void>> deleteVacancy(@PathVariable Long id, Authentication authentication) {
        Long employerId = getEmployerId(authentication);
        service.deleteVacancy(id, employerId);
        return ResponseEntity.ok().body(ApiResponse.success(null, "Vacancy successfully deleted"));
    }

    @GetMapping("/me")
    @Secured("ROLE_EMPLOYER")
    public ResponseEntity<ApiResponse<List<VacancyResponse>>> getMyVacancies(Authentication authentication) {
        Long employerId = getEmployerId(authentication);
        List<VacancyResponse> vacancies = service
                        .getMyVacancies(employerId)
                        .stream()
                        .map(VacancyMapper.INSTANCE::toVacancyResponse)
                        .toList();
        return ResponseEntity.ok().body(ApiResponse.success(vacancies, "Successfully found your vacancies"));
    }

    private Long getEmployerId(Authentication authentication) {
        return ((SecurityUser) authentication.getPrincipal()).id();
    }
}
