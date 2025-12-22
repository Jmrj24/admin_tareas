package com.example.tasks_service.service;

import com.example.tasks_service.dto.TaskCreateDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.dto.TaskResponseDTO;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.model.TaskStatus;

import java.util.List;

public interface ITaskService {
    // Metodo para crear tareas
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO);

    // Metodo para listar todas las tareas
    public List<TaskResponseDTO> getAllTasks();

    // Metodo para obtener una entidad de tarea por su id
    public Task getByIdEntityTask(Long idTask);

    // Metodo para obtener una tarea por su id
    public TaskResponseDTO getByIdTask(Long idTask);

    // Metodo para eliminar una tarea por su id
    public void deleteTask(Long idTask);

    // Metodo para editar una tarea
    public TaskResponseDTO editTask(Long idTask, TaskEditDTO taskDTO);

    // Metodo para obtener todas las tareas de un usuario
    public List<TaskResponseDTO> getAllTasksByIdUser(Long idUser);

    // Metodo para cambiar el estado de la tarea
    public TaskResponseDTO editStatusTask(Long idTask, TaskStatus status);
}
