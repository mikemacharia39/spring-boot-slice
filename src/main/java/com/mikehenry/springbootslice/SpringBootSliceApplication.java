package com.mikehenry.springbootslice;

import com.mikehenry.springbootslice.model.Task;
import com.mikehenry.springbootslice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class SpringBootSliceApplication implements CommandLineRunner {

	private final TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSliceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		taskService.insertTasks();

		log.info("Finding task by assigner and assignee");
		List<Task> taskList = taskService.findTaskByAssignerAndAssignee("Mike", "Zack");
		taskList.forEach(task -> log.info("Task name= {} Assigner= {} Assignee= {}",
				task.getTaskName(), task.getAssigner(), task.getAssignee()));
		log.info("Returned {} tasks", taskList.size());
	}
}
