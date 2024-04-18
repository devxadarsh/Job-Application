package org.devx.jobms.job.clients;

import org.devx.jobms.job.external.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name = "review-service")
@FeignClient(name = "REVIEW-SERVICE", url = "${review-service.url}") // It is when we dockerizing the MS
public interface ReviewClient {
    @GetMapping("/reviews")
    List<Review> getReview(@RequestParam("companyId") long id);
}
