package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Employee;
import com.mikehenry.springbootslice.repository.EmployeeRepository;
import com.mikehenry.springbootslice.util.Benchmark;
import com.mikehenry.springbootslice.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceUnit(unitName = "default")
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private final Random random = new Random();

    /**
     * Testing batch save
     */
    public void saveAll() {
        log.info("Saving all employees");
        List<String> employeeList = Arrays.asList("Ann", "Alex", "Bryan", "Ben", "Betty", "Grace", "Mike","Zack", "Wale", "Wren", "Paul", "Purity", "Peter", "Naomi");
        List<Employee> employees = new ArrayList<>();
        long currentStartTime = System.currentTimeMillis();

        int limit = 100000;
        for (int i = 0; i <= limit; i++) {
            String employeeName = employeeList.get(random.nextInt(employeeList.size()));

            Employee employee = new Employee();
            employee.setFirstName(employeeName);
            employee.setLastName(employeeName + i);
            employee.setEmailAddress(employeeName +"-"+ LocalDateTime.now() + "@mail.com");
            employee.setActive(1);
            employee.setChangeDetails(employeeName);
            employee.setDateModified(new Date());
            employee.setDateCreated(new Date());

            employees.add(employee);
        }
        log.info("Before save :: For {} records it took {} to prepare batch", limit, Benchmark.getTAT(currentStartTime));
        employeeRepository.saveAll(employees);

        log.info("After save :: For {} records it took {}", limit, Benchmark.getTAT(currentStartTime));
    }

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

    public void updateEmployeeDateModified(Long employeeID) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        if (optionalEmployee.isEmpty()) {
            log.error("EmployeeID " + employeeID + " is missing");
            return;
        }
        Employee employee = optionalEmployee.get();
        employee.setChangeDetails(employee.getFirstName() + "_" + System.currentTimeMillis());
        employeeRepository.save(employee);
        log.info("Successfully updated employee");
    }

    /**
     * Apparently, it appears while using slice, we cannot update records
     */
    public void findNullEmailAddress() {
        Pageable pageable = PageRequest.of(0, 3);

        boolean hasMoreSlices;
        do {
            Slice<Employee> employeeSlice = employeeRepository.findNullEmailAddresses(pageable);

            hasMoreSlices = employeeSlice.hasNext();

            List<Employee> employeeList = employeeSlice.getContent();

            processEmailUpdate(employeeList);

            pageable = employeeSlice.nextPageable();
        } while (hasMoreSlices);
    }

    private void processEmailUpdate(List<Employee> employeeList) {
        employeeList.forEach(employee -> {
            updateEmployeeEmails(employee.getEmployeeID());
        });
    }


    public void findByNullEmailAddress() {
        List<Employee> employeeList = employeeRepository.findByEmailAddressNull();

        employeeList.forEach(employee -> {
            updateEmployeeEmails(employee.getEmployeeID());
        });
    }

    /**
     * NB:// The update doesn't work
     *
     * @param employeeID employeeID
     */

    public void updateEmployeeEmails(Long employeeID) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeID);
        if (optionalEmployee.isEmpty()) {
            log.error("EmployeeID " + employeeID + " is missing");
            return;
        }
        Employee employee = optionalEmployee.get();
        String email = employee.getFirstName() + "." + employee.getLastName() + "@gmail.com";
        employee.setEmailAddress(email);
        employee.setActive(6);
        Employee updateEmployee = employeeRepository.save(employee);

        log.info("Successfully updated employee email " + updateEmployee.getEmailAddress());
    }


    /**
     * Use criteria query to find null email addresses
     */
    public void findByEmailAddressNullCQ() {
        List<Employee> employeeList = findByNullEmailAddressCriteriaQuery();
        log.info("Found {} records", employeeList.size());
        employeeList.forEach(this::updateEmployeeMail);
    }


    /**
     * Use criteria query to find null email addresses
     * @return list of null emails
     */
    public List<Employee> findByNullEmailAddressCriteriaQuery() {
        entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

        Root<Employee> employee = criteriaQuery.from(Employee.class);

        List<Predicate> predicateList = new ArrayList<>();

        predicateList.add(criteriaBuilder.isNull(employee.get("emailAddress")));

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    public void updateEmployeeMail(Employee employee) {
        log.info("updating employee");
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String email = employee.getFirstName() + "." + employee.getLastName() + "@gmail.com";
            employee.setEmailAddress(email);

            log.info("update email" + email);

            entityManager.merge(employee);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction() != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Error initializing entity manager  " + e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            log.error("closed entity manager");
        }

        log.info("completed employee update");
    }

    /**
     * Delete employee
     * @param employeeID employeeID
     */
    public void deleteEmployee(Long employeeID) {
        log.info("delete employee");
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Employee employee = entityManager.find(Employee.class, employeeID);
            entityManager.remove(employee);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if (entityManager.getTransaction() != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            log.error("Error initializing entity manager  " + e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            log.error("closed entity manager");
        }

        log.info("removed employee");
    }

}
