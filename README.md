# Sistema de Gestión de Tareas – Microservicios

## Descripción del Proyecto

Este proyecto consiste en un sistema de gestión de tareas desarrollado bajo una arquitectura de microservicios utilizando Spring Boot y Spring Cloud.

El sistema permite administrar tareas asociadas a usuarios, definiendo un ciclo de vida claro para cada tarea a través de distintos estados, fechas de vencimiento y notificaciones automáticas previas al vencimiento.

La arquitectura está compuesta por múltiples microservicios independientes, registrados dinámicamente mediante Eureka Server y expuestos a través de un API Gateway que actúa como punto único de entrada.

La configuración de todos los servicios se encuentra completamente externalizada y centralizada utilizando Spring Cloud Config Server, con un repositorio Git como fuente de configuración, permitiendo una gestión flexible y desacoplada de los entornos.

El sistema implementa reglas de negocio específicas, como la validación de usuarios existentes al crear tareas, el manejo de estados que restringen ciertas operaciones y la programación de notificaciones automáticas para tareas próximas a vencer.

## Arquitectura General

El sistema está diseñado bajo una arquitectura de microservicios utilizando Spring Cloud, con el objetivo de lograr desacoplamiento, escalabilidad y una clara separación de responsabilidades.

🔹 Componentes principales
🔸 Config Server

Centraliza la configuración de todos los microservicios.

Utiliza un repositorio Git como fuente de configuración.

Permite modificar propiedades de los servicios sin necesidad de recompilar ni redeplegar cada microservicio.

Ningún microservicio contiene archivos application.properties con configuración sensible o dependiente del entorno.

🔸 Eureka Server

Implementa el patrón Service Discovery.

Cada microservicio se registra dinámicamente al iniciarse.

Permite que los servicios se descubran entre sí sin depender de direcciones fijas.

🔸 API Gateway

Actúa como punto único de entrada al sistema.

Encargado del enrutamiento de solicitudes hacia los microservicios correspondientes.

Abstrae al cliente de la estructura interna del sistema.

🔹 Microservicios del dominio
🔸 Users Service

Gestiona la información de los usuarios.

Expone endpoints para consultar y validar la existencia de usuarios.

Es utilizado por otros servicios para validar reglas de negocio relacionadas con usuarios.

🔸 Tasks Service

Gestiona la creación, edición y seguimiento de tareas.

Implementa reglas de negocio como:

Asociación obligatoria a un usuario existente.

Manejo de estados de la tarea.

Validación de fechas de vencimiento.

Restricción de edición para tareas completadas.

Se comunica con el Users Service para validar la existencia de usuarios.

Programa notificaciones automáticas para tareas próximas a vencer.

Permite cancelar notificaciones futuras cuando la lógica de negocio lo requiere.

🔸 Notification Service

Gestiona el envío de notificaciones relacionadas con tareas.

Recibe solicitudes desde otro servicio y ejecuta el envío de notificaciones
a través de un proveedor externo de correo electrónico

🔹 Comunicación entre servicios

Los microservicios se comunican entre sí utilizando descubrimiento dinámico mediante Eureka.

No existen dependencias directas a direcciones IP o URLs fijas.

La arquitectura permite agregar o escalar servicios sin afectar al resto del sistema.

###Patrones de Arquitectura y Resiliencia

El sistema implementa distintos patrones de arquitectura orientados a la construcción de sistemas distribuidos resilientes y desacoplados.

🔹 Configuración Centralizada

Se utiliza Spring Cloud Config Server para externalizar la configuración de todos los microservicios, con un repositorio Git como fuente central de propiedades.
Esto permite modificar configuraciones sin recompilar ni redeplegar los servicios, favoreciendo la flexibilidad y mantenibilidad del sistema.

🔹 Service Registry y Discovery

Los microservicios se registran dinámicamente en Eureka Server, permitiendo el descubrimiento automático entre servicios y eliminando dependencias a direcciones fijas.

🔹 Comunicación entre Microservicios (Feign)

La comunicación entre microservicios se realiza mediante Spring Cloud OpenFeign, utilizando clientes declarativos para consumir servicios externos.

Permite definir interfaces que representan servicios remotos.

Se integra de forma nativa con Eureka para la resolución dinámica de servicios.

Utiliza Spring Cloud LoadBalancer para distribuir las solicitudes entre instancias disponibles.

Los clientes Feign están protegidos mediante Circuit Breaker con Resilience4j.

Este enfoque mejora la legibilidad del código, reduce el acoplamiento y facilita el manejo de fallos en sistemas distribuidos.

🔹 Load Balancing

El sistema utiliza Spring Cloud LoadBalancer para distribuir las solicitudes entre las distintas instancias disponibles de un microservicio.

El balanceo se realiza del lado del cliente.

Está integrado con Eureka para obtener las instancias disponibles.

Permite escalar servicios de forma transparente sin modificar el código.

🔹 Circuit Breaker y Resiliencia

Para manejar fallos en la comunicación entre microservicios, se implementa el patrón Circuit Breaker utilizando Resilience4j.

Este patrón se aplica en llamadas críticas, como:

Comunicación entre Tasks Service y Users Service.

Comunicación entre Tasks Service y Notification Service.

Comunicación entre Notification Service y Users Service.

El uso de Circuit Breaker permite:

Evitar fallos en cascada.

Definir comportamientos controlados ante errores.

Mejorar la estabilidad general del sistema.


Reglas de Negocio – Tasks Service

El Tasks Service es el núcleo del dominio del sistema y concentra la mayor parte de la lógica de negocio.
No se limita a operaciones CRUD, sino que implementa validaciones y reglas que garantizan la consistencia del sistema.

🔹 Creación de Tareas

Al crear una nueva tarea, se aplican las siguientes reglas:

El título es obligatorio y no puede estar vacío.

La descripción es opcional.

La tarea debe estar asociada a un usuario.

Antes de crear la tarea, se valida que el usuario exista consultando el Users Service.

El estado inicial de la tarea siempre es PENDING.

La fecha de creación se asigna automáticamente por el sistema.

La fecha de vencimiento, ingresada por el usuario, debe ser una fecha futura válida.

Opcionalmente, la tarea puede configurarse para enviar una notificación previa al vencimiento.

🔹 Edición de Tareas

Al editar una tarea existente:

El ID de la tarea no puede modificarse.

El usuario asignado no puede cambiarse.

Ningún campo es obligatorio durante la edición.

Solo se actualizan los campos que son enviados en la solicitud.

Los campos omitidos se mantienen sin cambios.

Si se modifica la fecha de vencimiento, se valida nuevamente que sea una fecha futura válida.

🔹 Restricciones por Estado

El estado de una tarea determina qué operaciones están permitidas:

Una tarea en estado COMPLETED no puede ser editada.

Las tareas pueden transicionar entre estados como:

PENDING

IN_PROGRESS

COMPLETED

Estas restricciones evitan inconsistencias en el ciclo de vida de la tarea.

🔹 Notificaciones y Tareas Futuras

El sistema permite asociar notificaciones automáticas a las tareas:

Al crear una tarea con notificación habilitada, se programa una notificación para ejecutarse una hora antes del vencimiento.

Si durante la edición se desactiva la notificación:

Se cancela la notificación futura previamente programada.

La gestión de notificaciones se realiza a través del Notification Service.

🔹 Manejo de Errores y Resiliencia

Las llamadas a otros microservicios se realizan utilizando Circuit Breaker.

Ante fallos en servicios externos:

Se evita la propagación de errores.

Se mantiene la estabilidad del sistema.

Se implementa manejo controlado de excepciones para escenarios de negocio inválidos.

Crear una tarea

POST /tasks

Crea una nueva tarea asociada a un usuario existente.

Reglas clave aplicadas:

El título es obligatorio.

El usuario debe existir.

El estado inicial es PENDING.

La fecha de creación se asigna automáticamente.

La fecha de vencimiento debe ser futura.

Request Body (ejemplo):

{
  "title": "Preparar presentación",
  "description": "Presentación para la reunión semanal",
  "dateExpiration": "2025-01-20T18:00:00",
  "priority": HIGH,
  "notifications": true,
  "userId": 1  
}

📍 Editar una tarea

PUT /tasks/{taskId}

Permite modificar ciertos campos de una tarea existente.

Reglas clave aplicadas:

No se puede modificar el ID ni el usuario asignado.

Ningún campo es obligatorio.

Solo se actualizan los campos enviados.

No se permite editar tareas completadas.

Request Body (ejemplo):

{
  "title": "Preparar presentación final",
  "dateExpiration": "2025-01-21T18:00:00",
  "notifications": false
}

📍 Obtener tareas

GET /tasks

Permite consultar tareas existentes, opcionalmente filtradas.

(Ejemplo de uso típico para listados o vistas generales)

🔁 Comunicación entre servicios

El Tasks Service valida la existencia del usuario mediante el Users Service.

Las notificaciones se gestionan a través del Notification Service.

Todas las comunicaciones entre servicios utilizan:

Service Discovery

Load Balancing

Circuit Breaker

## Microservicios

- **API Gateway**: punto único de entrada al sistema.
- **Service Registry (Eureka)**: descubrimiento dinámico de servicios.
- **Users Service**: gestión y validación de usuarios.
- **Tasks Service**: gestión de tareas y lógica de negocio principal.
- **Notification Service**: envío y programación de notificaciones.


## Flujo de una solicitud

Ejemplo: creación de una tarea

1. El cliente envía la solicitud al API Gateway.
2. El API Gateway enruta la petición al Tasks Service.
3. Tasks Service valida los datos de entrada.
4. Se consulta al Users Service (vía Feign) para validar la existencia del usuario.
5. Se crea la tarea con estado PENDING.
6. Si la notificación está habilitada, se comunica con Notification Service.
7. La respuesta final se devuelve al cliente a través del API Gateway.


## Tecnologías Utilizadas

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


## Ejecución del Proyecto

### Ejecución Local

Para ejecutar el proyecto de forma local, es necesario iniciar los servicios en el siguiente orden:

1. Config Server
2. Eureka Server
3. API Gateway
4. Users Service
5. Tasks Service
6. Notification Service

Cada servicio debe ejecutarse como una aplicación Spring Boot independiente.

Las configuraciones necesarias se obtienen automáticamente desde el Config Server, el cual consume las propiedades desde el repositorio Git configurado.

### Ejecución con Docker

El proyecto puede ejecutarse utilizando Docker para facilitar el despliegue de los distintos servicios.

Cada microservicio cuenta con su correspondiente Dockerfile y se puede levantar el sistema completo mediante Docker Compose.

Esto permite:
- Ejecutar todos los servicios de forma aislada.
- Simular un entorno distribuido.
- Simplificar la puesta en marcha del sistema.


## Variables de Entorno

El sistema utiliza variables de entorno para configurar aspectos sensibles y dependientes del entorno.

Algunas de las variables más relevantes son:

- `CONFIG_SERVER_URI`: URL del Config Server.
- `DB_URL`: URL de conexión a la base de datos.
- `DB_USERNAME`: usuario de la base de datos.
- `DB_PASSWORD`: contraseña de la base de datos.

Estas variables permiten adaptar el comportamiento del sistema sin modificar el código fuente.


🧠 Decisiones Técnicas

Durante el desarrollo del proyecto se tomaron diversas decisiones técnicas con el objetivo de construir un sistema mantenible, escalable y alineado con buenas prácticas de backend.

🔹 Arquitectura de Microservicios

Se optó por una arquitectura de microservicios para:

Separar responsabilidades por dominio.

Facilitar la escalabilidad y el mantenimiento.

Simular escenarios reales de sistemas distribuidos.

🔹 Configuración Centralizada

Se utilizó Spring Cloud Config Server con un repositorio Git como fuente de configuración para:

Externalizar configuraciones.

Evitar configuraciones hardcodeadas en los servicios.

Facilitar cambios sin necesidad de recompilación.

🔹 Comunicación entre Servicios con Feign

Se implementó Spring Cloud OpenFeign para la comunicación entre microservicios:

Mejora la legibilidad del código.

Reduce el acoplamiento.

Facilita la integración con mecanismos de resiliencia.

🔹 Resiliencia y Manejo de Fallos

Se implementó el patrón Circuit Breaker con Resilience4j para:

Evitar fallos en cascada.

Controlar errores en llamadas a servicios externos.

Aumentar la estabilidad del sistema.

🔹 Balanceo de Carga

Se utilizó Spring Cloud LoadBalancer para distribuir las solicitudes entre instancias disponibles:

Permite escalar servicios de forma transparente.

Elimina dependencias a instancias específicas.

🔹 Reglas de Negocio en el Backend

Se priorizó la implementación de reglas de negocio en el backend para:

Garantizar la consistencia del sistema.

Evitar dependencias en la lógica del cliente.

Controlar el ciclo de vida de las tareas.

Ejemplos:

No permitir editar tareas completadas.

Validar usuarios existentes antes de crear tareas.

Controlar fechas y estados.

🔹 Enfoque Backend

El proyecto se enfocó exclusivamente en el backend, priorizando:

Calidad del código.

Arquitectura.

Lógica de negocio.

La implementación de un frontend o mecanismos de seguridad avanzados se deja como posibles mejoras futuras.

## Mejoras Futuras

Algunas mejoras que podrían incorporarse al sistema son:

- Implementar autenticación y autorización utilizando Spring Security y JWT.
- Incorporar tests de integración entre microservicios.
- Implementar versionado de la API.

## Lecciones Aprendidas

Durante el desarrollo de este proyecto se adquirieron aprendizajes clave, entre ellos:

- La importancia de definir reglas de negocio claras y aplicarlas desde el backend.
- La complejidad real de los sistemas distribuidos y la necesidad de manejar fallos.
- El valor de la configuración centralizada en arquitecturas de microservicios.
- La diferencia entre dividir un sistema en servicios y diseñar una arquitectura distribuida.
- La relevancia de documentar correctamente un proyecto para hacerlo entendible a terceros.