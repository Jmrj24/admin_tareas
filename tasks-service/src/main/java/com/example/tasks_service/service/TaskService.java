package com.example.tasks_service.service;

import com.example.tasks_service.dto.NotificationsDTO;
import com.example.tasks_service.dto.TaskDTO;
import com.example.tasks_service.model.Task;
import com.example.tasks_service.repository.IApiNotifications;
import com.example.tasks_service.repository.ITaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    final private Map<String, ScheduledFuture<?>> TaskMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    @Override
    public void createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDateCreation(LocalDateTime.now());
        task.setDateExpiration(taskDTO.getDateExpiration());
        task.setState(taskDTO.getState());
        task.setPriority(taskDTO.getPriority());
        task.setNotifications(taskDTO.isNotifications());
        task.setIdUser(taskDTO.getIdUser());
        taskRepo.save(task);
        if(task.isNotifications()) {
            expirationNotification(task);
            sendNotifications(task, "la tarea ha sido creada de forma exitosa, el dia "
                    + task.getDateCreation().format(formato) + " y su estado es: " + task.getState());
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Task getByIdTask(Long idTask) {
        return taskRepo.findById(idTask).orElse(null);
    }

    @Override
    public void deleteTask(Long idTask) {
        taskRepo.deleteById(idTask);
    }

    @Override
    public void editTask(Task task) {
        taskRepo.save(task);
        if(task.isNotifications()) {
            sendNotifications(task, "la tarea ha sido actualizada de forma exitosa y su estado es: "
                    + task.getState());
            if(task.getState().equalsIgnoreCase("COMPLETADA")) {
                ScheduledFuture<?> taskFuture = TaskMap.get("task"+task.getId());
                taskFuture.cancel(true);
            }
        }
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
            sendNotifications(task, "la tarea está por expirar y su estado es: "+ task.getState());
        }, delaySeconds, TimeUnit.MILLISECONDS);
    }

    private void fallbackMethodSendNotifications(Throwable throwable) {
        logger.error("ALERTA INTERNA: Circuit Breaker activado. No se pudo enviar la notificación.", throwable);
    }
}
