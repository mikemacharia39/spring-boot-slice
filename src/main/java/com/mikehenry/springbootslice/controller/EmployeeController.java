package com.mikehenry.springbootslice.controller;

import com.mikehenry.springbootslice.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("employee")
@RestController
@AllArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("processRecords")
    @ResponseStatus(HttpStatus.OK)
    public String processRecords() {
        log.info("Received request to process request");

        employeeService.getEmployeeLesserThanCurrentDate();

        return "Completed";
    }

    @PutMapping("updateNullEmails")
    @ResponseStatus(HttpStatus.OK)
    public String updateNullEmails() {
        log.info("Received request to process request");

        employeeService.findByEmailAddressNullCQ();

        return "Completed";
    }
}
