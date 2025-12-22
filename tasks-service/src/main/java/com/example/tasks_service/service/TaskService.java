package com.example.tasks_service.service;

import com.example.tasks_service.dto.TaskCreateDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.dto.TaskResponseDTO;
import com.example.tasks_service.exception.BadRequestExceptionTask;
import com.example.tasks_service.exception.ConflictExceptionTask;
import com.example.tasks_service.exception.NotFoundExceptionTask;
import com.example.tasks_service.infrastructure.ExternalApiService;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.model.TaskStatus;
import com.example.tasks_service.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService implements ITaskService {
    @Autowired
    private ITaskRepository taskRepo;

    @Autowired
    private ExternalApiService externalApiService;

    final private Map<String, ScheduledFuture<?>> TaskMap = new HashMap<>();

    // Crear una instancia de ScheduledExecutorService
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    @Override
    public TaskResponseDTO createTask(TaskCreateDTO taskCreateDTO) {
        if(taskCreateDTO.dateExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestExceptionTask("Fecha de expiracion de la tarea invalida");
        }
        externalApiService.ValidateUserById(taskCreateDTO.idUser());

        Task task = new Task();
        task.setTitle(taskCreateDTO.title());
        task.setDescription(taskCreateDTO.description());
        task.setDateCreation(LocalDateTime.now());
        task.setDateExpiration(taskCreateDTO.dateExpiration());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(taskCreateDTO.priority());
        task.setNotifications(taskCreateDTO.notifications());
        task.setIdUser(taskCreateDTO.idUser());
        if(task.isNotifications()) {
            expirationNotification(task);
            externalApiService.sendNotifications(task, "la tarea ha sido creada de forma exitosa, el dia "
                    + task.getDateCreation().format(formato) + " y su estado es: " + task.getStatus());
        }
        Task taskSaved = taskRepo.save(task);
        return toTaskResponse(taskSaved);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(this::toTaskResponse)
                .toList();
    }

    @Override
    public Task getByIdEntityTask(Long idTask) {
        return taskRepo.findById(idTask).orElseThrow(() -> new NotFoundExceptionTask("Tarea no Encontrada"));
    }

    @Override
    public TaskResponseDTO getByIdTask(Long idTask) {
        return toTaskResponse(getByIdEntityTask(idTask));
    }

    @Override
    public void deleteTask(Long idTask) {
        taskRepo.deleteById(idTask);
    }

    @Override
    public TaskResponseDTO editTask(Long idTask, TaskEditDTO taskEditDTO) {
        Task taskEdit = getByIdEntityTask(idTask);
        if(taskEdit.getStatus().equals(TaskStatus.COMPLETED)) {
            throw new ConflictExceptionTask("Tarea no editable, ya que su estado es " + TaskStatus.COMPLETED);
        }
        if(taskEditDTO.getDateExpiration()!=null) {
            if(taskEditDTO.getDateExpiration().isBefore(LocalDateTime.now())) {
                throw new BadRequestExceptionTask("Fecha de expiracion de la tarea invalida");
            }
            taskEdit.setDateExpiration(taskEditDTO.getDateExpiration());
        }
        if(taskEditDTO.getTitle()!=null) {
            taskEdit.setTitle(taskEditDTO.getTitle());
        }
        if(taskEditDTO.getDescription()!=null) {
            taskEdit.setDescription(taskEditDTO.getDescription());
        }
        if(taskEditDTO.getStatus()!=null) {
            taskEdit.setStatus(taskEditDTO.getStatus());
            if(taskEdit.getStatus().equals(TaskStatus.COMPLETED)) {
                ScheduledFuture<?> taskFuture = TaskMap.get("task"+taskEdit.getId());
                if(taskFuture!=null) {
                    taskFuture.cancel(true);
                }
            }
        }
        if(taskEditDTO.getPriority()!=null) {
            taskEdit.setPriority(taskEditDTO.getPriority());
        }
        if(taskEditDTO.getNotifications()!=null) {
            taskEdit.setNotifications(taskEditDTO.getNotifications());
        }
        if(taskEdit.isNotifications()) {
            externalApiService.sendNotifications(taskEdit, "la tarea ha sido actualizada de forma exitosa y su estado es: "
                    + taskEdit.getStatus());
        }
        Task taskSaved = taskRepo.save(taskEdit);
        return toTaskResponse(taskSaved);
    }

    // Listar todas las tareas de un usuario
    @Override
    public List<TaskResponseDTO> getAllTasksByIdUser(Long idUser) {
        return taskRepo.getAllTaskByIdUser(idUser);
    }

    @Override
    public TaskResponseDTO editStatusTask(Long idTask, TaskStatus status) {
        Task taskEdit = getByIdEntityTask(idTask);
        if(taskEdit.getStatus().equals(TaskStatus.COMPLETED)) {
            throw new ConflictExceptionTask("Tarea no editable, ya que su estado es " + TaskStatus.COMPLETED);
        }
        taskEdit.setStatus(status);
        Task taskSaved = taskRepo.save(taskEdit);
        return toTaskResponse(taskSaved);
    }

    private TaskResponseDTO toTaskResponse (Task task) {
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.getDateCreation(), task.getDateExpiration(),
                task.getStatus(), task.getPriority(), task.isNotifications(), task.getIdUser());
    }

    private void expirationNotification(Task task) {
        // Configurar la fecha y hora específicas
        LocalDateTime targetDateTime = task.getDateExpiration();
        LocalDateTime update = targetDateTime.minusHours(1);

        // Calcular el retraso en milisegundos entre la hora actual y la fecha objetivo
        long delay = Duration.between(LocalDateTime.now(), update).toMillis();

        // Programar tareas y almacenarlas en el map
        TaskMap.put("task"+task.getId(), scheduleTask(scheduler, delay, task));
    }

    // Metodo para programar una tarea con un tiempo específico
    private ScheduledFuture<?> scheduleTask(ScheduledExecutorService scheduler, long delaySeconds, Task task) {
        return scheduler.schedule(() -> {
            externalApiService.sendNotifications(task, "la tarea está por expirar y su estado es: "+ task.getStatus());
        }, delaySeconds, TimeUnit.MILLISECONDS);
    }
}