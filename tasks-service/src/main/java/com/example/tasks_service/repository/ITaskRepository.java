package com.example.tasks_service.repository;

import com.example.tasks_service.dto.TaskResponseDTO;
import com.example.tasks_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT ta FROM Task ta WHERE ta.idUser=:idUser")
    List<TaskResponseDTO> getAllTaskByIdUser(Long idUser);
}
