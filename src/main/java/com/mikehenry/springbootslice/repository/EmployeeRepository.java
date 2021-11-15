package com.mikehenry.springbootslice.repository;

import com.mikehenry.springbootslice.model.Employee;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e from employees e where dateCreated < ?1")
    Slice<Employee> findByDateCreated(String date);
}
