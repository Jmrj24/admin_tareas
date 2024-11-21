# Sistema de Gestión de Tareas

Este proyecto es una aplicación basada en microservicios para la gestión de tareas. Está diseñada para ser escalable, modular y fácilmente integrable con otros sistemas.

## Funcionalidades

- Gestión de usuarios:
  - Creación, actualización y eliminación de usuarios.
- Gestión de tareas:
  - Creación, actualización y eliminación de tareas.
  - Relación de tareas con usuarios.
- Notificaciones:
  - Envío de notificaciones por correo electrónico sobre cambios relevantes en las tareas o usuarios.
- Configuración centralizada:
  - Uso de Spring Cloud Config para centralizar configuraciones de los microservicios.
- Registro y descubrimiento:
  - Eureka para la gestión de servicios.

## Arquitectura

El sistema está compuesto por los siguientes microservicios:

1. **Eureka Server**:
   - Servicio de registro y descubrimiento para microservicios.

2. **Config Server**:
   - Centraliza la configuración de todos los microservicios.

3. **API Gateway**:
   - Punto de entrada único para enrutar solicitudes a los microservicios correspondientes.

4. **Users Service**:
   - Administra los usuarios del sistema.

5. **Tasks Service**:
   - Administra las tareas asignadas a los usuarios.

6. **Notifications Service**:
   - Envía notificaciones relacionadas con los eventos del sistema.

## Requisitos

- **Java 17**
- **Spring Boot 3.1+**
- **Docker**
- **MySQL 8.3+**

Docker Compose
El archivo docker-compose.example.yml está incluido en este repositorio para que puedas levantar los servicios fácilmente. Recuerda copiarlo como docker-compose.yml

## Tecnologías utilizadas
- **Spring Boot (con Spring Cloud Config, Eureka, etc.)**
- **Docker**
- **MySQL**
- **Postman (para pruebas)**
- **JPA/Hibernate**