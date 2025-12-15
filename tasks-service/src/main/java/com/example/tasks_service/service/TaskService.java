package com.example.tasks_service.service;

import com.example.tasks_service.dto.NotificationsDTO;
import com.example.tasks_service.dto.TaskDTO;
import com.example.tasks_service.dto.TaskEditDTO;
import com.example.tasks_service.exception.BadRequestException;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.model.TaskStatus;
import com.example.tasks_service.repository.IApiNotifications;
import com.example.tasks_service.repository.IApiUsers;
import com.example.tasks_service.repository.ITaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private IApiNotifications apiNotifications;

    @Autowired
    private IApiUsers apiUsers;

    final private Map<String, ScheduledFuture<?>> TaskMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    @Override
    public Task createTask(TaskDTO taskDTO) {
        if(apiUsers.getUserById(taskDTO.getIdUser()).isEmpty()) {
            throw new NotFoundException("Usuario No existe");
        }
        if(taskDTO.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Fecha de expiracion de la tarea invalida");
        }
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDateCreation(LocalDateTime.now());
        task.setDateExpiration(taskDTO.getDateExpiration());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(taskDTO.getPriority());
        task.setNotifications(taskDTO.isNotifications());
        task.setIdUser(taskDTO.getIdUser());
        if(task.isNotifications()) {
            expirationNotification(task);
            sendNotifications(task, "la tarea ha sido creada de forma exitosa, el dia "
                    + task.getDateCreation().format(formato) + " y su estado es: " + task.getStatus());
        }
        return taskRepo.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Task getByIdTask(Long idTask) {
        return taskRepo.findById(idTask).orElseThrow(() -> new NotFoundException("Tarea no Encontrada"));
    }

    @Override
    public void deleteTask(Long idTask) {
        taskRepo.deleteById(idTask);
    }

    @Override
    public Task editTask(Long idTask, TaskEditDTO taskEditDTO) {
        if(!taskRepo.existsById(idTask)) {
            throw new NotFoundException("Tarea no Encontrada");
        }
        if(taskEditDTO.getStatus().equals(TaskStatus.COMPLETED)) {
            throw new BadRequestException("Tarea no editable, ya que su estado es Completada");
        }
        if(taskEditDTO.getDateExpiration().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Fecha de expiracion de la tarea invalida");
        }
        Task taskEdit = getByIdTask(idTask);
        taskEdit.setTitle(taskEditDTO.getTitle());
        taskEdit.setDescription(taskEditDTO.getDescription());
        taskEdit.setDateExpiration(taskEditDTO.getDateExpiration());
        taskEdit.setStatus(taskEditDTO.getStatus());
        taskEdit.setPriority(taskEditDTO.getPriority());
        taskEdit.setNotifications(taskEditDTO.isNotifications());
        if(taskEdit.getStatus().equals(TaskStatus.COMPLETED)) {
            ScheduledFuture<?> taskFuture = TaskMap.get("task"+taskEdit.getId());
            if(taskFuture!=null) {
                taskFuture.cancel(true);
            }
        }
        if(taskEdit.isNotifications()) {
            sendNotifications(taskEdit, "la tarea ha sido actualizada de forma exitosa y su estado es: "
                    + taskEdit.getStatus());
        }
        return taskRepo.save(taskEdit);
    }

    @Override
    public List<Task> getAllTasksByIdUser(Long idUser) {
        return taskRepo.getAllTaskByIdUser(idUser);
    }

    public void expirationNotification(Task task) {
        // Crear una instancia de ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Configurar la fecha y hora específicas
        LocalDateTime targetDateTime = task.getDateExpiration();
        LocalDateTime update = targetDateTime.minusHours(1);

        // Calcular el retraso en milisegundos entre la hora actual y la fecha objetivo
        long delay = Duration.between(LocalDateTime.now(), update).toMillis();

        // Programar tareas y almacenarlas en el map
        TaskMap.put("task"+task.getId(), scheduleTask(scheduler, delay, task));
    }

    // Genera las notificaciones
    @CircuitBreaker(name = "notifications-service", fallbackMethod = "fallbackMethodSendNotifications")
    @Retry(name = "notifications-service")
    public void sendNotifications(Task task, String text) {
        NotificationsDTO notificationsDTO = new NotificationsDTO(task.getTitle(), text , task.getIdUser());
        apiNotifications.sendNotification(notificationsDTO);
    }

    // Metodo para programar una tarea con un tiempo específico
    public ScheduledFuture<?> scheduleTask(ScheduledExecutorService scheduler, long delaySeconds, Task task) {
        return scheduler.schedule(() -> {
            sendNotifications(task, "la tarea está por expirar y su estado es: "+ task.getStatus());
        }, delaySeconds, TimeUnit.MILLISECONDS);
    }

    private void fallbackMethodSendNotifications(Throwable throwable) {
        logger.error("ALERTA INTERNA: Circuit Breaker activado. No se pudo enviar la notificación.", throwable);
    }
}
