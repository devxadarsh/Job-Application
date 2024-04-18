package org.devx.jobms.job;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.devx.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobms")
// Here at class level RequestMapping will change the base url by adding /jobs for entire class
// Then we do not need to put /jobs for all other mappings
public class JobController {
//    private List<Job> jobs = new ArrayList<>();

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll() {
        return new ResponseEntity<>(jobService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    //Here if I use @RequestMapping at class level then implementation will be like below.
//    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job) {
        jobService.createJob(job);
        return new ResponseEntity<>("Job created Successfully", HttpStatus.CREATED);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<JobDTO> getJobByID(@PathVariable Long id) {
        JobDTO jobDTO = jobService.getJobById(id);
        if (jobDTO != null) {
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        }
        return new ResponseEntity<> (HttpStatus.NOT_FOUND);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteId(@PathVariable Long id) {
        Job job = jobService.deleteJobById(id);
        if (job != null) {
            return new ResponseEntity<>( "Job Id deleted Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<> ("Id not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
//    @RequestMapping(path = "/jobs/{id}", method = RequestMethod.PUT)
    //we can use Requestmapping with method and class both
    public ResponseEntity<String> updateJob(@RequestBody Job updateJob, @PathVariable Long id) {
        Job oldJob = jobService.updateJobById(updateJob,id);
        if (oldJob != null) {
            return new ResponseEntity<>("Job updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<> ("Job not found or Id & API mismatch",HttpStatus.NOT_FOUND);
    }

//It is not good prictice for response it will not give us the data that server responding on call
//To know all the details and better control on the code we use the ResponseEntity generic which is provided on springboot
//    @GetMapping ("/jobs/{id}")
//    public Job getJobByID(@PathVariable Long id) {
//        Job job  = jobService.getJobById(id);
//        if (job != null) {
//            return job;
//        }
//        return new Job(0L, "Test case", "Test case", "Test case", "Test case", "Test case");
//
//    }
}
