# 📦 Mensajeria-Entornos-Backend

## 📌 Descripción
Este proyecto es una plataforma digital diseñada para optimizar la gestión de pedidos y entregas en empresas de mensajería express. Permite a las empresas asignar pedidos a mensajeros, realizar seguimiento en tiempo real y mejorar la organización de sus operaciones. 

## ✨ Características Principales
- Registro y autenticación de usuarios.
- Registro y administración de empresas.
- Registro y administración de clientes.
- Creación y administración de pedidos.
- Seguimiento de historial de pedidos.

## 🛢️ Base de datos
En el apartado de /src/main se encuentra:
- schema.sql con el diseño de la base de datos.
- data.sql con información para prueba de la aplicación.

## 🔧 Requisitos Previos
- Java 17 o superior
- Maven 3.6+
- MySQL 
- IDE recomendado: VS Code

## ⚙️ Instalación y Configuración
### 1️⃣ Clonar el Repositorio
```bash
git clone https://github.com/silviac43/Mensajeria-Entornos-Backend.git
cd Mensajeria-Entornos-Backend
```
### 2️⃣ Configurar el Backend
1. Instalar dependencias:
```bash
mvn clean install
```
2. Configurar la base de datos en `application.properties` con tus datos.
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/mensajeria
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```
3. Iniciar el servidor:
```bash
mvn spring-boot:run
```
## 📡 Principales Endpoints

| Método(s)                           | Endpoint                  | Descripción                                   |
|-------------------------------------|---------------------------|-----------------------------------------------|
| `POST`, `PUT`, `GET`                | `/api/auth/`              | Autenticación y gestión de sesión de usuarios |
| `POST`, `PUT`, `GET`, `DELETE`      | `/api/clientes`           | CRUD completo de clientes                     |
| `POST`, `PUT`, `GET`, `DELETE`      | `/api/empresas`           | CRUD completo de empresas                     |
| `GET`                               | `/api/historialpedido`    | Obtener historial de pedidos                  |
| `POST`, `PUT`, `GET`, `DELETE`      | `/api/mensajeros`         | CRUD completo de mensajeros                   |
| `POST`, `PUT`, `GET`, `DELETE`      | `/api/pedidos`            | CRUD completo de pedidos                      |
| `POST`, `PUT`, `GET`, `DELETE`      | `/api/admin/user`         | Gestión administrativa de usuarios            |


## 💬 Autores
- Dilan Esteban Rey Sepulveda - 2190397
- Silvia Alejandra Cárdenas Santos - 2210102
