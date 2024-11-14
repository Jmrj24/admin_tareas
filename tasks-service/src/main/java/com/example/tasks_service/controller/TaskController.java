package com.example.tasks_service.controller;

import com.example.tasks_service.dto.TaskDTO;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public String createTask(@RequestBody TaskDTO taskDTO) {
        taskServ.createTask(taskDTO);
        return "Tarea creada exitosamente!";
    }

    @GetMapping("/get")
    public List<Task> getAllTasks() {
        return taskServ.getAllTasks();
    }

    @DeleteMapping("/delete/{idTask}")
    public String deleteTask(@PathVariable Long idTask) {
        taskServ.deleteTask(idTask);
        return "Tarea eliminada de manera exitosa!";
    }

    @PutMapping("/edit")
    public Task editTask(@RequestBody Task task) {
        taskServ.editTask(task);
        return taskServ.getByIdTask(task.getId());
    }

    @GetMapping("/get/{idUser}")
    public List<Task> getAllTasksByIdUser(@PathVariable Long idUser) {
        // Creado para verificar el funcionamiento del LoadBalancing
        //System.out.println("-------------Desde: " + serverPort);
        return taskServ.getAllTasksByIdUser(idUser);
    }
}
