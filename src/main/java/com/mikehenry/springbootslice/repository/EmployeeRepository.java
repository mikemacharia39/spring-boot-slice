package com.mikehenry.springbootslice.repository;

import com.mikehenry.springbootslice.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.stream.Stream;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // you can't use findAll with JpaRepository use CrudRepository
    // reason - JpaRepository extends PagingAndSortingRepository, which is not implemented by slice
    // Slice<Employee> findAll(Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE dateCreated <= :dateCreated")
    Slice<Employee> findByDateCreated(Date dateCreated, Pageable pageable);

    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("SELECT e FROM Employee e WHERE emailAddress IS NULL")
    Stream<Employee> findByNullEmailAddress();
}
