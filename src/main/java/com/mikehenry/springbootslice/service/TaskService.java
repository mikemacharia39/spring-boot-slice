package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Employee;
import com.mikehenry.springbootslice.model.Task;
import com.mikehenry.springbootslice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private EntityTransaction entityTransaction = null;

    List<String> assignerList = Arrays.asList("Ann", "Alex", "Bryan", "Ben", "Betty", "Grace", "Mike");
    List<String> assigneeList = Arrays.asList("Zack", "Wale", "Wren", "Paul", "Purity", "Peter", "Naomi");

    public void insertTasks() {
        log.info("Creating tasks");
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityTransaction = entityManager.getTransaction();

            entityTransaction.begin();

            Random  random = new Random();

            Optional<Employee> optionalEmployee = employeeRepository.findById(1L);
            Employee employee = optionalEmployee.get();

            for (int i = 0; i < 10; i++) {
                String assigner = assignerList.get(random.nextInt(assignerList.size()));
                String assignee = assigneeList.get(random.nextInt(assigneeList.size()));

                Task task = new Task();
                task.setEmployee(employee);
                task.setTaskName("Task " + i);
                task.setAssigner(assigner);
                task.setAssignee(assignee);
                task.setDescription("A task about " + i);

                log.info("persisting task " + task.getTaskName());

                entityManager.persist(task);
            }

            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            log.error("failed initializing entity manager  " + e.getMessage());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            log.error("closed entity manager");
        }

        log.info("completed task creation");
    }

    public List<Task> findTaskByAssignerAndAssignee(String assigner, String assignee) {
        entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);

        Root<Task> task = criteriaQuery.from(Task.class);

        List<Predicate> predicateList = new ArrayList<>();

        if (assigner != null && assignee != null) {
            predicateList.add(criteriaBuilder.equal(task.get("assigner"), assigner));
            predicateList.add(criteriaBuilder.equal(task.get("assignee"), assignee));
        }

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Return tasks by employee name
     * @param employeeName employeeName
     * @return List<Tasks> associated with employee
     */
    public List<Task> findTaskByEmployeeNameAsList(String employeeName) {
        entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Task> typedQuery = entityManager.createQuery(
                "SELECT tsk FROM Employee e " +
                        "INNER JOIN e.tasks tsk " +
                        "WHERE e.firstName = :firstName",
                Task.class);

        List<Task> taskList = typedQuery
                .setParameter("firstName", employeeName)
                .getResultList();

        log.info("{} has {} tasks", employeeName, taskList.size());

        return taskList;
    }

    /**
     * Return tasks by employee name
     * @param employeeName employeeName
     * @return List<Tasks> associated with employee
     */
    public Slice<Task> findTaskByEmployeeNameAsSlice(String employeeName, Pageable pageable) {
        entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Task> typedQuery = entityManager.createQuery(
                "SELECT tsk FROM Employee e " +
                        "INNER JOIN e.tasks tsk " +
                        "WHERE e.firstName = :firstName",
                Task.class);

        int pageSize = pageable.getPageSize();
        int offset = pageable.getPageNumber() > 0 ? pageable.getPageNumber() * pageSize : 0;

        typedQuery.setMaxResults(pageSize + 1);
        typedQuery.setFirstResult(offset);

        List<Task> taskList = typedQuery
                .setParameter("firstName", employeeName)
                .getResultList();

        boolean hasNext =  pageable.isPaged() && taskList.size() > pageSize;

        return new SliceImpl<>(hasNext ? taskList.subList(0, pageSize) : taskList, pageable, hasNext);
    }

    /**
     * To update task details
     * @param taskList task
     */
    public void updateListOfTaskDetails(List<Task> taskList) {
        log.info("updating tasks");
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            taskList.forEach(task -> entityManager.merge(task));

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

        log.info("updated task update");
    }

    public void updateTask(Task task, String searchString) {
        task.setTaskName("Task by " + searchString);
        task.setDescription("Task was created by " + searchString);
    }
}
