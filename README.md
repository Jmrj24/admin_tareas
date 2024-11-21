Sistema de Gestión de Tareas
Este es un proyecto backend basado en microservicios, diseñado para gestionar tareas con características como creación, asignación, notificaciones y más. Es ideal para demostrar habilidades en el desarrollo de aplicaciones distribuidas con Java y Spring.

Características
Arquitectura de Microservicios: Servicios independientes que interactúan entre sí.
Configuración Centralizada: Implementado con Spring Cloud Config.
Service Discovery: Manejo de servicios dinámicos con Eureka.
Gateway API: Punto de entrada único para las solicitudes.
Circuit Breaker: Resiliencia mediante gestión de fallos.
Notificaciones: Integración para enviar correos electrónicos en eventos clave.
Arquitectura y Comunicación entre Microservicios
El sistema está compuesto por tres microservicios principales que interactúan de la siguiente manera:

1. Servicio de Usuarios
Maneja el registro, actualización y gestión de usuarios.
Se comunica con:
Servicio de Tareas: Proporciona información sobre usuarios asociados a tareas.
Servicio de Notificaciones: Genera notificaciones relacionadas con los usuarios.
2. Servicio de Tareas
Permite la creación, actualización y eliminación de tareas.
Informa al Servicio de Notificaciones sobre eventos clave (nuevas tareas, actualizaciones, etc.).
Depende del Servicio de Usuarios para asignar tareas a responsables.
3. Servicio de Notificaciones
Recibe eventos de los otros servicios y genera notificaciones (por ejemplo, correos electrónicos).
Configurado para manejar tanto mensajes directos como eventos asincrónicos en el futuro.
Comunicación
Todos los microservicios están registrados en Eureka para el descubrimiento de servicios.
Las solicitudes pasan a través del API Gateway, lo que asegura un punto de entrada centralizado.
Circuit Breaker garantiza que fallos en un servicio no afecten a los demás.
Tecnologías Usadas
Java 17
Spring Boot y Spring Cloud
Docker y Docker Compose
MySQL como base de datos principal
Postman para pruebas de API
Pruebas
Los endpoints han sido probados utilizando Postman.
El envío de notificaciones funciona correctamente cuando se cumplen los eventos configurados.
Próximos Pasos
Incorporar un sistema de autenticación y autorización (como JWT o API Keys).
Mejorar la documentación detallada de los endpoints.
