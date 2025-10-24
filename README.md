# java-hello-world

Ejemplo de aplicación Java "Hello World" desplegada en un servidor WebLogic dentro de un contenedor Docker.
## Requisitos
- Docker
- Maven
- Java JDK 17 o superior
## Construcción de la imagen Docker
1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu_usuario/java-hello-world.git
   cd java-hello-world
   ```
2. Construye la imagen Docker:
   ```bash
   docker build -t hello-wls:1.0 .
   ```
## Ejecución del contenedor
Puedes ejecutar el contenedor pasando las credenciales de administrador como variables de entorno:
```bash
docker run -e ADMIN_USERNAME={your_username} -e ADMIN_PASSWORD={your_password} -p 7001:7001 hello-wls:1.0
```