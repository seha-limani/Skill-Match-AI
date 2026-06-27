package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.model.Company;
import com.example.SkillMatch_AI.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<Company> all() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public Company byId(@PathVariable Long id) {
        return companyService.getById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public List<Company> mine() {
        return companyService.getMine();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Company create(@Valid @RequestBody Company company) {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Company update(@PathVariable Long id, @Valid @RequestBody Company company) {
        return companyService.update(id, company);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public void delete(@PathVariable Long id) {
        companyService.delete(id);
    }
}