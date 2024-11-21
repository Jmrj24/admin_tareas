# Administrador de Tareas

## Descripción

Este proyecto es una aplicación para gestionar tareas dentro de un sistema de microservicios. Diseñada para facilitar la creación, asignación y seguimiento de tareas, esta aplicación permite una administración eficiente de las tareas asignadas a los usuarios. Utiliza **Spring Boot** para los microservicios y **Eureka** para el descubrimiento de servicios, con una arquitectura modular que facilita la escalabilidad y mantenibilidad del sistema.

El sistema no cuenta con mecanismos de autenticación por el momento, pero se ha construido sobre una arquitectura de microservicios que permite integrar más servicios en el futuro, como seguridad, autentificación, entre otros.

## Funcionalidades principales

- **Gestión de Tareas**: Permite crear, editar, eliminar y consultar las tareas. Las tareas pueden tener un título, descripción, fecha de vencimiento y estado.
- **Gestión de Usuarios**: Este servicio se encarga de gestionar la información básica de los usuarios, como su creación y edición. Los usuarios son identificados por su ID, el cual es utilizado por el servicio de tareas para asignarles tareas específicas.
- **Notificaciones**: Envía notificaciones a los usuarios sobre las tareas creadas, modificadas o próximas a vencer.

## Modelado de la Aplicación

El sistema está basado en tres entidades principales:

1. **Tarea**: Contiene detalles sobre las tareas, como el título, descripción, fecha de vencimiento, estado (pendiente, en progreso, completada) y la asignación de usuario.
2. **Usuario**: Contiene la información de los usuarios, como nombre y email. Los usuarios se asignan a tareas por su ID.
3. **Notificación**: Gestiona las notificaciones enviadas a los usuarios cuando ocurren eventos importantes relacionados con las tareas.

## Arquitectura de Microservicios

La aplicación está compuesta por los siguientes microservicios:

- **Task Service (Servicio de Tareas)**: Gestiona las operaciones relacionadas con las tareas (crear, editar, eliminar, consultar).
- **User Service (Servicio de Usuarios)**: Gestiona la información de los usuarios, quienes se asignan a las tareas a través de su ID.
- **Notification Service (Servicio de Notificaciones)**: Envia notificaciones a los usuarios cuando ocurren eventos en las tareas.
- **Config Server (Servicio de Configuración)**: Centraliza y distribuye las configuraciones para todos los microservicios.
- **Eureka Server**: Facilita el descubrimiento y la comunicación entre los servicios sin necesidad de configurar direcciones IP fijas.
- **API Gateway**: Actúa como el punto de entrada para las solicitudes externas, redirigiéndolas a los microservicios correspondientes.

## Tecnologías Utilizadas

- **Lenguaje**: Java
- **Framework**: Spring Boot (para crear la API)
- **Microservicios**: Spring Cloud (para el descubrimiento de servicios)
- **Base de Datos**: MySQL (para almacenar tareas, usuarios)
- **JPA/Hibernate** (para el manejo de la persistencia de datos)
- **Eureka** (para el descubrimiento de servicios)
- **Docker** (para contenerización y facilitar el despliegue)
- **Postman** (para realizar pruebas de la API)
- **Git y GitHub** (para control de versiones y colaboración)

## Endpoints Principales

- **GET /task/get**: Lista todas las tareas disponibles.
- **POST /task/create**: Crea una nueva tarea.
- **PUT /task/edit**: Edita los detalles de una tarea.
- **DELETE /task/delete/{id}**: Elimina una tarea por su ID.
- **GET /user/get**: Lista todos los usuarios registrados.
- **POST /user/create**: Registra un nuevo usuario.


