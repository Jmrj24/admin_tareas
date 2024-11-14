package com.example.tasks_service.service;

import com.example.tasks_service.dto.TaskDTO;
import com.example.tasks_service.model.Task;

import java.util.List;

public interface ITaskService {
    // Metodo para crear tareas
    public void createTask(TaskDTO taskDTO);

    // Metodo para listar todas las tareas
    public List<Task> getAllTasks();

    // Metodo para obtener una tarea por su id
    public Task getByIdTask(Long idTask);

    // Metodo para eliminar una tarea por su id
    public void deleteTask(Long idTask);

    // Metodo para editar una tarea
    public void editTask(Task task);

    // Metodo para obtener todas las tareas de un usuario
    public List<Task> getAllTasksByIdUser(Long idUser);
}
