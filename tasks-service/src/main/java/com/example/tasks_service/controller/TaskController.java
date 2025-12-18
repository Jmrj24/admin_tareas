package com.example.tasks_service.controller;

import com.example.tasks_service.dto.TaskCreateDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.service.ITaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskServ;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskServ.createTask(taskCreateDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskServ.getAllTasks());
    }

    @DeleteMapping("/delete/{idTask}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long idTask) {
        taskServ.deleteTask(idTask);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/edit/{idTask}")
    public ResponseEntity<Task> editTask(@PathVariable Long idTask, @Valid @RequestBody TaskEditDTO taskEditDTO) {
        return ResponseEntity.ok(taskServ.editTask(idTask, taskEditDTO));
    }

    @GetMapping("/get/user/{idUser}")
    public ResponseEntity<List<Task>> getAllTasksByIdUser(@PathVariable Long idUser) {
        return ResponseEntity.ok(taskServ.getAllTasksByIdUser(idUser));
    }
}
