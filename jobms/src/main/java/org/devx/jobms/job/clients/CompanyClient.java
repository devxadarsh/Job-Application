package org.devx.jobms.job.clients;

import org.devx.jobms.job.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name = "company-service")
@FeignClient(name = "COMPANY-SERVICE", url = "${company-service.url}") // It is when we dockerizing the MS
public interface CompanyClient {
    @GetMapping("/company/{id}")
    Company getCompanyId(@PathVariable("id") Long companyId);
}
