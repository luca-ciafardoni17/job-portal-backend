package com.eazybytes.jobportal.feature.company.service;

import com.eazybytes.jobportal.feature.company.dto.CompanyDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    List<CompanyDto> getAllCompaniesForAdmin();

    boolean createCompany(CompanyDto companyDto);

    boolean updateCompanyDetails(Long id, @Valid CompanyDto companyDto);

    void deleteCompanyById(Long id);
}
