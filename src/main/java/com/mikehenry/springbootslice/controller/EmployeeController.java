package com.mikehenry.springbootslice.controller;

import com.mikehenry.springbootslice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("employee")
@RestController
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("processRecords")
    @ResponseStatus(HttpStatus.OK)
    public String processRecords() {
        employeeService.getEmployeeLesserThanCurrentDate();
        return "Completed";
    }
}
