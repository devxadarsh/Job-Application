package org.devx.jobms.job.impl;


import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.devx.jobms.job.Job;
import org.devx.jobms.job.JobRepository;
import org.devx.jobms.job.JobService;
import org.devx.jobms.job.clients.CompanyClient;
import org.devx.jobms.job.clients.ReviewClient;
import org.devx.jobms.job.dto.JobDTO;
import org.devx.jobms.job.external.Company;
import org.devx.jobms.job.external.Review;
import org.devx.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;


    int retryAttempts = 0;

    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
//    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: " + ++retryAttempts);
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();
//        RestTemplate restTemplate = new RestTemplate();
//        for (Job job : jobs) {
//            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
//            jobWithCompanyDTO.setJob(job);
//            Company company = restTemplate.getForObject("http://localhost:8081/company/1", Company.class);
//            jobWithCompanyDTO.setCompany(company);
//            jobWithCompanyDTOs.add(jobWithCompanyDTO);
//        }
//        return jobWithCompanyDTOs;
        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e) {
        List<String> messages = new ArrayList<>();
        messages.add("e.getMessage()");
        return messages;
    }

    private JobDTO convertToDTO(Job job) {
//        RestTemplate restTemplate = new RestTemplate();
//        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
//        jobWithCompanyDTO.setJob(job);

//        Company company = restTemplate.getForObject(
//                "http://company-service:8081/company/" + job.getCompanyId(),
//                Company.class);
        // we can simplify the code using openfien client service here is the code to perform same task
        Company company = companyClient.getCompanyId(job.getCompanyId());

//        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                "http://riview-service:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<Review>>() {});
//        List<Review> reviews = reviewResponse.getBody();
        // we can simplify the code using openfien client service here is the code to perform same task
        List<Review> reviews = reviewClient.getReview(job.getCompanyId());

        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
        return jobDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job =  jobRepository.findById(id).orElse(null);
        return convertToDTO(job);
    }

    @Override
    public Job deleteJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        jobRepository.deleteById(id);
        return job;
    }

    @Override
    public Job updateJobById(Job updatedJob,Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setLocation(updatedJob.getLocation());
            return jobRepository.save(job);
        }
        return null;
    }


}
