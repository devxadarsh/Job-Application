package org.devx.companyms.company;

import org.devx.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> findAllCompanies();

    void createCompany(Company company);

    boolean deleteCompany(Long id);

    boolean updateCompany(Company company,Long id);

    Company getCompanyById(Long id);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}
