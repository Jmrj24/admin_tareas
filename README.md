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
`POST /tasks/create`

```json
{
  "title": "Preparar presentación",
  "description": "Presentación para la reunión semanal",
  "dateExpiration": "2025-01-20T18:00:00",
  "priority": "HIGH",
  "notifications": true,
  "userId": 1
}
```
Editar una Tarea

PATCH /tasks//edit/{idTask}
```json
{
  "title": "Preparar presentación final",
  "dateExpiration": "2025-01-21T18:00:00",
  "notifications": false
}
```
Obtener Tareas

GET /tasks/get

---

## 🔄 Flujo de una Solicitud

**Ejemplo: creación de una tarea**

1. El cliente envía la solicitud al **API Gateway**.
2. El **API Gateway** enruta la petición al **Tasks Service**.
3. El **Tasks Service** valida los datos de entrada.
4. Se valida la existencia del usuario mediante el **Users Service** (vía Feign).
5. Se crea la tarea con estado inicial `PENDING`.
6. Si la notificación está habilitada, se comunica con el **Notification Service**.
7. La respuesta final se devuelve al cliente a través del **API Gateway**.

---

## 🧪 Postman Collection

El repositorio incluye una **Postman Collection** lista para utilizar, que permite probar los endpoints del sistema a través del **API Gateway**.

📁 **/postman/SistemaAdminTareas.postman_collection.json**


La colección cubre los principales flujos del sistema, incluyendo:

- Gestión de usuarios.
- Creación y edición de tareas.
- Validación de reglas de negocio.
- Comunicación entre microservicios.

### 🌍 Entornos de Postman

La colección está configurada para trabajar con **dos entornos**:

- **Local**: para ejecución del proyecto en entorno local o con Docker.
- **Render**: para pruebas sobre el sistema desplegado en la nube.

Cada entorno define variables como la URL base del API Gateway, evitando modificar manualmente los endpoints.

📌 Para utilizar la colección:
1. Importar el archivo de la colección en Postman.
2. Seleccionar el entorno correspondiente (Local o Render).
3. Ejecutar las requests disponibles.

---

## 🛠️ Tecnologías Utilizadas

### Backend
- Java
- Spring Boot
- Spring Cloud
- Spring Cloud Config
- Spring Cloud OpenFeign
- Spring Cloud Gateway
- Eureka Server
- Resilience4j
- JPA / Hibernate
- MySQL

### Infraestructura
- Docker

### Testing / Herramientas
- Postman

---

## 🚀 Ejecución del Proyecto

### Ejecución Local

Para ejecutar el proyecto de forma local, iniciar los servicios en el siguiente orden:

1. Config Server
2. Eureka Server
3. API Gateway
4. Users Service
5. Tasks Service
6. Notification Service

Cada servicio se ejecuta como una aplicación Spring Boot independiente y obtiene su configuración desde el **Config Server**.

---

### 🐳 Ejecución del Proyecto con Docker

El sistema puede ejecutarse de forma completa utilizando **Docker y Docker Compose**, permitiendo levantar todos los microservicios y sus dependencias de manera orquestada.

Esta opción es ideal para simular un entorno distribuido de forma local.

### 📋 Requisitos Previos

- Docker
- Docker Compose

### ⚙️ Configuración del Entorno

Antes de ejecutar el proyecto, es necesario configurar las variables de entorno.

1. Copiar el archivo de ejemplo:

.env.example

2. Renombrarlo a:

.env

3. Completar los valores según el entorno local o de pruebas.

Este archivo será utilizado por `docker-compose.yml` para inyectar las variables necesarias en cada microservicio.

### 🚀 Levantar el Sistema

Desde la raíz del proyecto, ejecutar:
docker-compose build
docker-compose up

Este comando realizará las siguientes acciones:

- Construirá las imágenes de cada microservicio.
- Iniciará los contenedores.
- Registrará los servicios automáticamente en Eureka Server.
- Centralizará la configuración a través de Config Server.
- Expondrá el sistema a través del API Gateway.

### 📡 Acceso al Sistema

Una vez levantado el entorno:

- Los endpoints pueden probarse utilizando la **Postman Collection** incluida en el repositorio.

### 🛑 Detener la Ejecución

Para detener y eliminar los contenedores:

docker-compose down

Este comando detiene el sistema sin eliminar las imágenes construidas.

---

## 🔐 Variables de Entorno

Las variables de entorno se definen por microservicio y permiten adaptar el sistema a distintos entornos sin modificar el código fuente.

### Variables comunes
- `CONFIG_SERVER_URI`

### Servicios con Base de Datos (Users / Tasks)
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

> Cada microservicio posee su propia base de datos y configuración independiente, aunque los nombres de las variables sean los mismos.

### 📄 Archivo .env.example

El repositorio incluye un archivo `.env.example` que sirve como referencia para configurar el entorno.

Antes de ejecutar el proyecto (especialmente con Docker), se debe:

1. Copiar el archivo `.env.example`
2. Renombrarlo a `.env`
3. Completar los valores según el entorno

Este archivo es consumido por `docker-compose.yml` para inyectar las variables necesarias en cada microservicio.

---

## 🧠 Decisiones Técnicas

- Arquitectura de microservicios orientada a dominio.
- Configuración centralizada mediante Spring Cloud Config Server.
- Comunicación declarativa entre servicios utilizando OpenFeign.
- Implementación de resiliencia con Circuit Breaker (Resilience4j).
- Balanceo de carga del lado del cliente con Spring Cloud LoadBalancer.
- Reglas de negocio implementadas exclusivamente en el backend.

---

## 🔮 Mejoras Futuras

- Implementar autenticación y autorización con Spring Security y JWT.
- Incorporar tests de integración entre microservicios.
- Implementar versionado de la API.

---

## 📚 Lecciones Aprendidas

- La importancia de definir y aplicar reglas de negocio desde el backend.
- La complejidad real de los sistemas distribuidos.
- El valor de la configuración centralizada en arquitecturas de microservicios.
- La importancia de documentar correctamente un proyecto para terceros.
