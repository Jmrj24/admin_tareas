# Sistema de Gestión de Tareas – Arquitectura de Microservicios

## 📌 Descripción del Proyecto

Este proyecto consiste en un sistema de gestión de tareas desarrollado bajo una arquitectura de **microservicios**, utilizando **Spring Boot** y **Spring Cloud**.

El sistema permite administrar tareas asociadas a usuarios, definiendo un ciclo de vida claro para cada tarea mediante distintos estados, fechas de vencimiento y notificaciones automáticas previas al vencimiento.

La arquitectura está compuesta por múltiples microservicios independientes, registrados dinámicamente mediante **Eureka Server** y expuestos a través de un **API Gateway**, que actúa como punto único de entrada.

La configuración de todos los servicios se encuentra completamente externalizada y centralizada utilizando **Spring Cloud Config Server**, con un repositorio Git como fuente de configuración, permitiendo una gestión flexible y desacoplada de los entornos.

Además, el sistema implementa reglas de negocio específicas como:

- Validación de usuarios existentes al crear tareas  
- Restricciones de operaciones según el estado de la tarea  
- Programación y cancelación de notificaciones automáticas  

---

## 🏗️ Arquitectura General

El sistema está diseñado bajo una arquitectura de microservicios orientada al **desacoplamiento**, la **escalabilidad** y la **separación clara de responsabilidades**.

### 🔹 Componentes Principales

#### 🔸 Config Server
- Centraliza la configuración de todos los microservicios.
- Utiliza un repositorio Git como fuente de configuración.
- Permite modificar propiedades sin recompilar ni redeplegar servicios.
- Ningún microservicio contiene configuraciones sensibles en su código.

#### 🔸 Eureka Server
- Implementa el patrón **Service Discovery**.
- Los microservicios se registran dinámicamente al iniciar.
- Elimina dependencias a direcciones IP o URLs fijas.

#### 🔸 API Gateway
- Punto único de entrada al sistema.
- Encargado del enrutamiento de solicitudes.
- Abstrae al cliente de la estructura interna del sistema.

---

## 🧩 Microservicios del Dominio

### 🔸 Users Service
- Gestiona la información de los usuarios.
- Expone endpoints para consultar y validar usuarios.
- Es utilizado por otros servicios para validar reglas de negocio.

### 🔸 Tasks Service
- Gestiona la creación, edición y seguimiento de tareas.
- Implementa reglas de negocio como:
  - Asociación obligatoria a un usuario existente.
  - Manejo de estados de la tarea.
  - Validación de fechas de vencimiento.
  - Restricción de edición para tareas completadas.
- Se comunica con Users Service para validar usuarios.
- Programa y cancela notificaciones automáticas.

### 🔸 Notification Service
- Gestiona el envío de notificaciones relacionadas con tareas.
- Recibe solicitudes desde otros servicios.
- Envía notificaciones mediante un proveedor externo de correo electrónico.

---

## 🔁 Comunicación entre Servicios

- Descubrimiento dinámico mediante **Eureka**.
- Sin dependencias a URLs o IPs fijas.
- Comunicación resiliente y escalable.
- Posibilidad de escalar servicios sin afectar al sistema.

---

## ♻️ Patrones de Arquitectura y Resiliencia

### 🔹 Configuración Centralizada
Uso de **Spring Cloud Config Server** con repositorio Git como fuente central de propiedades.

### 🔹 Service Registry & Discovery
Registro y descubrimiento automático de servicios mediante **Eureka Server**.

### 🔹 Comunicación entre Microservicios (Feign)
- Uso de **Spring Cloud OpenFeign**.
- Interfaces declarativas para servicios remotos.
- Integración nativa con Eureka.
- Load Balancing con Spring Cloud LoadBalancer.
- Protección mediante Circuit Breaker (Resilience4j).

### 🔹 Load Balancing
- Balanceo del lado del cliente.
- Integración con Eureka.
- Escalado transparente de servicios.

### 🔹 Circuit Breaker y Resiliencia
Implementado con **Resilience4j** para llamadas críticas:
- Tasks Service → Users Service
- Tasks Service → Notification Service
- Notification Service → Users Service

Beneficios:
- Evita fallos en cascada.
- Define comportamientos controlados ante errores.
- Mejora la estabilidad del sistema.

---

## 📐 Reglas de Negocio – Tasks Service

El **Tasks Service** concentra la mayor parte de la lógica de negocio.

### 🔹 Creación de Tareas
- El título es obligatorio.
- La descripción es opcional.
- Debe asociarse a un usuario existente.
- Validación del usuario vía Users Service.
- Estado inicial: `PENDING`.
- Fecha de creación automática.
- Fecha de vencimiento futura obligatoria.
- Notificación opcional previa al vencimiento.

### 🔹 Edición de Tareas
- El ID no puede modificarse.
- El usuario asignado no puede cambiarse.
- Ningún campo es obligatorio.
- Solo se actualizan campos enviados.
- Validación de fechas si se modifican.
- No se permiten ediciones si el estado es `COMPLETED`.

### 🔹 Estados de la Tarea
Estados permitidos:
- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`

Restricción:
- Las tareas `COMPLETED` no pueden editarse.

### 🔹 Notificaciones
- Programación automática una hora antes del vencimiento.
- Cancelación automática si se desactiva la notificación.
- Gestión a través del Notification Service.

---

## 📌 Endpoints Principales

### Crear una Tarea
`POST /tasks`

```json
{
  "title": "Preparar presentación",
  "description": "Presentación para la reunión semanal",
  "dateExpiration": "2025-01-20T18:00:00",
  "priority": "HIGH",
  "notifications": true,
  "userId": 1
}

Editar una Tarea

PUT /tasks/{taskId}

{
  "title": "Preparar presentación final",
  "dateExpiration": "2025-01-21T18:00:00",
  "notifications": false
}

Obtener Tareas

GET /tasks

🔄 Flujo de una Solicitud

Ejemplo: creación de una tarea

Cliente → API Gateway

API Gateway → Tasks Service

Validación de datos

Validación de usuario vía Users Service

Creación de la tarea

Comunicación con Notification Service (si aplica)

Respuesta al cliente

🛠️ Tecnologías Utilizadas
Backend

Java

Spring Boot

Spring Cloud

Spring Cloud Config

Spring Cloud OpenFeign

Spring Cloud Gateway

Eureka Server

Resilience4j

JPA / Hibernate

MySQL

Infraestructura

Docker

Testing / Herramientas

Postman

🚀 Ejecución del Proyecto
Ejecución Local

Orden de arranque:

Config Server

Eureka Server

API Gateway

Users Service

Tasks Service

Notification Service

Ejecución con Docker

Dockerfile por microservicio.

Orquestación con Docker Compose.

Simulación de entorno distribuido.

🔐 Variables de Entorno

Variables utilizadas:

CONFIG_SERVER_URI

DB_URL

DB_USERNAME

DB_PASSWORD

Permiten adaptar el sistema sin modificar el código fuente.

🧠 Decisiones Técnicas

Arquitectura de microservicios orientada a dominio.

Configuración centralizada.

Comunicación declarativa con Feign.

Resiliencia con Circuit Breaker.

Balanceo de carga del lado del cliente.

Reglas de negocio implementadas en el backend.

🔮 Mejoras Futuras

Autenticación y autorización con Spring Security y JWT.

Tests de integración entre microservicios.

Versionado de la API.

📚 Lecciones Aprendidas

Importancia de las reglas de negocio en backend.

Complejidad real de sistemas distribuidos.

Valor de la configuración centralizada.

Diferencia entre dividir servicios y diseñar una arquitectura distribuida.

Importancia de documentar para terceros.