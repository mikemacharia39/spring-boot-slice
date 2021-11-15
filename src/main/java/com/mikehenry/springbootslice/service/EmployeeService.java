package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Employee;
import com.mikehenry.springbootslice.repository.EmployeeRepository;
import com.mikehenry.springbootslice.util.Utility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void getEmployeeLesserThanCurrentDate() {

        int pageNumber = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        boolean hasMoreRecords;

        long startTime = System.currentTimeMillis();

        do {
            Slice<Employee> employeeSlice = employeeRepository.findByDateCreated(new Date(), pageable);
            hasMoreRecords = employeeSlice.hasNext();
            int sliceNumber = employeeSlice.getNumber();
            int sliceSize = employeeSlice.getNumberOfElements();

            log.info("Slice number= {}", sliceNumber + " number of elements= " + sliceSize);

            List<Employee> employeeList = employeeSlice.getContent();

            employeeList.forEach(employee -> updateEmployeeDateModified(employee.getEmployeeID()));

            pageable = employeeSlice.nextPageable();
        } while (hasMoreRecords);

        log.info("Completed" + Utility.getTAT(startTime));
    }

    private void updateEmployeeDateModified(Long employeeID) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        if (optionalEmployee.isEmpty()) {
            log.error("EmployeeID " + employeeID + " is missing");
            return;
        }
        Employee employee = optionalEmployee.get();
        employee.setChangeDetails(employee.getFirstName() + "_" + System.currentTimeMillis());
        log.error("Successfully updated employee");
        employeeRepository.save(employee);
    }
}
