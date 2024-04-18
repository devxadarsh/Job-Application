package org.devx.companyms.company.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("REVIEW-SERVICE")
@FeignClient(name = "REVIEW-SERVICE", url = "${review-service.url}") // It is when we dockerizing the MS
public interface ReviewClient {
    @GetMapping("/reviews/averageReview")
    Double getAverageRatingForCompany(@RequestParam("companyId") Long companyId);
}
