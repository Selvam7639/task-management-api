package com.tms.taskmanagementapi.service;

import com.tms.taskmanagementapi.constants.TaskConstants;
import com.tms.taskmanagementapi.dto.ResponseDto;
import com.tms.taskmanagementapi.dto.TaskDto;
import com.tms.taskmanagementapi.entity.Task;
import com.tms.taskmanagementapi.exception.ResourceNotFoundException;
import com.tms.taskmanagementapi.mapper.ResponseMapper;
import com.tms.taskmanagementapi.mapper.TaskMapper;
import com.tms.taskmanagementapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;


    public ResponseDto addTask(TaskDto taskDto) {
        Task task = TaskMapper.mapToTask(taskDto);
        taskRepository.save(task);
        return ResponseMapper
                .mapToResponseDto(HttpStatus.CREATED, TaskConstants.MESSAGE_201);
    }

    public List<Task> getAllTasks(String status) {
        List<Task> tasks = taskRepository.findAll();
        return getTasks(tasks, status);
    }

    private List<Task> getTasks(List<Task> tasks, String status) {

        if (!status.isBlank() || !status.isEmpty() || !status.equalsIgnoreCase("All")) {

            return tasks
                    .stream()
                    .filter(task -> task.getStatus().equalsIgnoreCase(status))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return tasks;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(TaskConstants.EXCEPTION_MESSAGE_TASK_NOT_FOUND, id)));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> searchByName(String searchString, String status) {
        List<Task> tasks = taskRepository.findByNameContaining(searchString);

        return getTasks(tasks, status);
    }


}
