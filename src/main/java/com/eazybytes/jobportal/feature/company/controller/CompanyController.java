package com.eazybytes.jobportal.feature.company.controller;

import com.eazybytes.jobportal.feature.company.dto.CompanyDto;
import com.eazybytes.jobportal.feature.company.service.ICompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor    //implementa constructor Autowired di companyService
public class CompanyController {

    private final ICompanyService companyService;

    @GetMapping(path = "/public", version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companyDtoList = this.companyService.getAllCompanies();
        return ResponseEntity.ok().body(companyDtoList);
    }

    @GetMapping(path = "/admin")
    public ResponseEntity<List<CompanyDto>> getAllCompaniesForAdmin() {
        List<CompanyDto> companyDtoList = this.companyService.getAllCompaniesForAdmin();
        return ResponseEntity.ok().body(companyDtoList);
    }

    @PostMapping(path = "/admin")
    public ResponseEntity<String> createCompany(@RequestBody CompanyDto companyDto) {
        boolean isCreated = this.companyService.createCompany(companyDto);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Company created successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Company creation failed!");
        }
    }

    @PutMapping(path = "/{id}/admin")
    public ResponseEntity<String> updateCompanyDetails(
            @PathVariable @NotBlank String id,
            @RequestBody @Valid CompanyDto companyDto
    ) {
        boolean isUpdated = companyService.updateCompanyDetails(Long.valueOf(id), companyDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Company details updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Company details");
        }
    }

    @DeleteMapping(path = "/{id}/admin")
    public ResponseEntity<String> deleteCompanyById(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Company record deleted successfully.");
    }

}
