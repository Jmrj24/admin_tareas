package com.example.tasks_service.controller;

import com.example.tasks_service.dto.TaskCreateDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.dto.TaskResponseDTO;
import com.example.tasks_service.model.TaskStatus;
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
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskServ.createTask(taskCreateDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskServ.getAllTasks());
    }

    @DeleteMapping("/delete/{idTask}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long idTask) {
        taskServ.deleteTask(idTask);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/edit/{idTask}")
    public ResponseEntity<TaskResponseDTO> editTask(@PathVariable Long idTask, @Valid @RequestBody TaskEditDTO taskEditDTO) {
        return ResponseEntity.ok(taskServ.editTask(idTask, taskEditDTO));
    }

    @PatchMapping("/status/{idTask}")
    public ResponseEntity<TaskResponseDTO> editStatusTask(@PathVariable Long idTask, @RequestBody TaskStatus status) {
        return ResponseEntity.ok(taskServ.editStatusTask(idTask, status));
    }

    @GetMapping("/get/user/{idUser}")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasksByIdUser(@PathVariable Long idUser) {
        return ResponseEntity.ok(taskServ.getAllTasksByIdUser(idUser));
    }
}
