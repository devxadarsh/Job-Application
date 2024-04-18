package org.devx.companyms.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> findAll() {
        return ResponseEntity.ok(companyService.findAllCompanies());
    }

    @PostMapping
    public ResponseEntity<String> addCompany(@RequestBody Company company) {
        companyService.createCompany(company);
        return new ResponseEntity<>("Company created Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if(company != null)
            return new ResponseEntity<>(company,HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

//        if(companyService.getCompanyById(id) != null) {
//            return new ResponseEntity<>(companyService.getCompanyById(id), HttpStatus.OK);
//        }
//        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean response = companyService.deleteCompany(id);
        if(response)
        return new ResponseEntity<>("Company deleted Successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Company Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@RequestBody Company company, @PathVariable Long id) {
        boolean response = companyService.updateCompany(company,id);
        if(response)
            return new ResponseEntity<>("Company Updated Successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Company Not Found", HttpStatus.NOT_FOUND);
    }


}
