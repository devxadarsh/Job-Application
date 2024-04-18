package org.devx.jobms.job;

import org.devx.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> findAll();

    void createJob(Job job);

    JobDTO getJobById(Long id);

    Job deleteJobById(Long id);

    Job updateJobById(Job updatedJob,Long id);


}
