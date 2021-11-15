package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Employee;
import com.mikehenry.springbootslice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public void getEmployeeLesserThanCurrentDate() {

        Slice<Employee> employeeSlice = null;

        Pageable pageable = PageRequest.of(0, 3);

        String currDate = LocalDateTime.now().toString();

        while (employeeSlice.hasNext()) {

            int sliceNumber = employeeSlice.getNumber();
            int sliceSize = employeeSlice.getNumberOfElements();

            log.info("Slice number {0}", sliceNumber, " number of elements {1}", sliceSize);

            employeeSlice = employeeRepository.findByDateCreated(currDate);

            List<Employee> employeeList = employeeSlice.getContent();

            employeeList.forEach(employee -> {
                updateEmployeeDateModified(employee.getEmployeeID());
            });
        }
    }

    private void updateEmployeeDateModified(Long employeeID) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        if (optionalEmployee.isPresent()) {
            log.error("EmployeeID " + employeeID + " is missing");
            return;
        }
        Employee employee = optionalEmployee.get();
        employee.setChangeDetails(employee.getFirstName() + "_" + System.currentTimeMillis());

        employeeRepository.save(employee);
    }
}
