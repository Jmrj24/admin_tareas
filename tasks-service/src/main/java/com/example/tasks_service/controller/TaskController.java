package com.example.tasks_service.controller;

import com.example.tasks_service.dto.TaskDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.service.ITaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskServ;

    @Value("${server.port}")
    private int serverPort;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskServ.createTask(taskDTO));
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

    @GetMapping("/get/{idUser}")
    public List<Task> getAllTasksByIdUser(@PathVariable Long idUser) {
        // Creado para verificar el funcionamiento del LoadBalancing
        //System.out.println("-------------Desde: " + serverPort);
        return taskServ.getAllTasksByIdUser(idUser);
    }
}
