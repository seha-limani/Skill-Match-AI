package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.model.Company;

import java.util.List;

public interface CompanyService {
    Company create(Company company);
    Company update(Long id, Company company);
    void delete(Long id);
    Company getById(Long id);
    List<Company> getAll();
    List<Company> getMine();
}