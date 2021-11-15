package com.mikehenry.springbootslice.controller;

import com.mikehenry.springbootslice.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
