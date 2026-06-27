package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.model.Company;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.model.Enum.Role;
import com.example.SkillMatch_AI.repository.CompanyRepository;
import com.example.SkillMatch_AI.service.CompanyService;
import com.example.SkillMatch_AI.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Company create(Company company) {
        User currentUser = currentUserService.get();
        company.setUser(currentUser);
        if (company.getVerified() == null) {
            company.setVerified(false);
        }
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public Company update(Long id, Company company) {
        Company existing = getById(id);
        ensureOwnerOrAdmin(existing.getUser());
        existing.setName(company.getName());
        existing.setDescription(company.getDescription());
        existing.setWebsite(company.getWebsite());
        existing.setLogo(company.getLogo());
        existing.setLocation(company.getLocation());
        existing.setVerified(company.getVerified());
        return companyRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Company existing = getById(id);
        ensureOwnerOrAdmin(existing.getUser());
        companyRepository.delete(existing);
    }

    @Override
    public Company getById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getMine() {
        User currentUser = currentUserService.get();
        return companyRepository.findByUserId(currentUser.getId());
    }

    private void ensureOwnerOrAdmin(User owner) {
        User currentUser = currentUserService.get();
        if (!currentUser.getId().equals(owner.getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Not allowed");
        }
    }
}