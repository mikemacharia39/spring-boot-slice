package com.mikehenry.springbootslice;

import com.mikehenry.springbootslice.model.Task;
import com.mikehenry.springbootslice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
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

		log.info("Finding task by assigner and assignee =============================");
		List<Task> taskList = taskService.findTaskByAssignerAndAssignee("Mike", "Zack");
		taskList.forEach(task -> log.info("Task name= {} Assigner= {} Assignee= {}",
				task.getTaskName(), task.getAssigner(), task.getAssignee()));
		log.info("Returned {} tasks", taskList.size());

		/**
		log.info("Finding tasks by employee name ==============================");
		List<Task> taskList1 = taskService.findTaskByEmployeeNameAsList("Mikehenry");
		log.info("Returned {} tasks", taskList1.size());
		*/

		log.info("Using slice ==============================");

		Pageable pageable = PageRequest.of(0, 40000);
		boolean hasMoreRecords;
		int chunkNumber = 0;
		String searchString = "Mikehenry";
		do {
			Slice<Task> taskSlice = taskService.findTaskByEmployeeNameAsSlice(searchString, pageable);
			hasMoreRecords = taskSlice.hasNext();

			int chunkSize = taskSlice.getNumberOfElements();
			chunkNumber++;

			List<Task> taskListFromSlice = taskSlice.getContent();
			List<Task> updatedTaskDetails = new ArrayList<>();
			taskListFromSlice.forEach(task -> {
				task.setTaskName("Task by " + searchString);
				task.setDescription("Task was created by " + searchString);
				updatedTaskDetails.add(task);
			});

			taskService.updateListOfTaskDetails(updatedTaskDetails);

			log.info("Chunk number: {}  chunk size: {}", chunkNumber, chunkSize);

			pageable = taskSlice.nextPageable();
			Thread.sleep(100);
		} while (hasMoreRecords);

	}
}
