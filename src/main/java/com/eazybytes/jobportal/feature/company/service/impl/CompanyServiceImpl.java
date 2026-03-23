package com.eazybytes.jobportal.feature.company.service.impl;

import com.eazybytes.jobportal.constants.AppConstants;
import com.eazybytes.jobportal.feature.company.dto.CompanyDto;
import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.company.entity.Company;
import com.eazybytes.jobportal.feature.job.entity.Job;
import com.eazybytes.jobportal.feature.company.repository.CompanyRepository;
import com.eazybytes.jobportal.feature.company.service.ICompanyService;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    @Cacheable(value = "companies", key = "'user'")
    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> activeCompanies = companyRepository.findAllWithJobsByStatus(AppConstants.ACTIVE_STATUS);
        return activeCompanies.stream().map(TransformationUtil::transformCompanyToDto).toList();
    }

    @Cacheable(value = "companies", key = "'admin'")
    @Override
    public List<CompanyDto> getAllCompaniesForAdmin() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(TransformationUtil::transformCompanyToDtoForAdmin).collect(Collectors.toList());
    }

    @CacheEvict(value = "companies", allEntries = true)
    @Override
    @Transactional
    public boolean createCompany(CompanyDto companyDto) {
        Company company = TransformationUtil.transformCompanyDtoToEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return savedCompany.getId() != null && savedCompany.getId() > 0;
    }

    @CacheEvict(value = "companies", allEntries = true)
    @Override
    @Transactional
    public boolean updateCompanyDetails(Long id, CompanyDto companyDto) {
        int updatedRecords = companyRepository.updateCompanyDetails(
                id, companyDto.name(), companyDto.logo(), companyDto.industry(),companyDto.size(),companyDto.rating(),
                companyDto.locations(),companyDto.founded(),companyDto.description(), companyDto.employees(),companyDto.website()
        );
        return updatedRecords > 0;
    }

    @CacheEvict(value = "companies", allEntries = true)
    @Override
    @Transactional
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

}
