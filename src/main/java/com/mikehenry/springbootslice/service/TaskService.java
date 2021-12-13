package com.mikehenry.springbootslice.service;

import com.mikehenry.springbootslice.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class TaskService {

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

            for (int i = 0; i < 10; i++) {
                String assigner = assignerList.get(random.nextInt(assignerList.size()));
                String assignee = assigneeList.get(random.nextInt(assigneeList.size()));

                Task task = new Task();
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

}
