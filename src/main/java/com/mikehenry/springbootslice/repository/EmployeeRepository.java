package com.mikehenry.springbootslice.repository;

import com.mikehenry.springbootslice.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // you can't use findAll with JpaRepository use CrudRepository
    // reason - JpaRepository extends PagingAndSortingRepository, which is not implemented by slice
    // Slice<Employee> findAll(Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE dateCreated <= :dateCreated")
    Slice<Employee> findByDateCreated(Date dateCreated, Pageable pageable);
}
