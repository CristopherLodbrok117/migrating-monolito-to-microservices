# Mudando de monolito a microservicios

Cambiaremos la forma en que trabajamos la API de la aplicación Sinaloa APP. Tomaremos el sistema de archivos integrado y crearemos un microservicio donde ofrezca sus servicios. Crearemos
otro mas para dejar el resto de los servicios.

Justificación: A diferencia del servicio de archivos, el resto de los servicios tiene una cantidad de relaicones a nivel de base de datos muy robusta. El sistema de archivos es ideal para
este ejemplo, pues no cuenta con relaciones significativas. Y estas pueden ser solucionadas comunicando eventualmente ambos microservicios.

## Creación de proyecto

Crearemos un microservicio padre en [Spring initializr](https://start.spring.io/). De momento no tendra ninguna dependencia, solo elegimos la versión del lenguaje, maven y generaremos el 
proyecto. Una vez descomprimido dejaremos únicamente el archivo .gitignore y el pom.xml.

Generaremos dos nuevos archivos, cada uno con las dependencias correspondientes, uno para el servicio de repositorio de archivos y otro para el resto de los servicios (opcionalmente
se pueden crear mas si se desea desacoplar ams servicios)

Descomprimimos ambos proyectos y copiamos las carpetas dentro del microservicio padre

Estructura

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/58%20-%20project.png" alt="estructura de microservicios" width="700">

<br>

## Configuración

Editaremos los archivos pom.xml para que todo funcione correctamente.

Proyecto principal pom.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.4.5</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>app</groupId>
	<artifactId>microservices</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<packaging>pom</packaging>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<maven.compiler.source>21</maven.compiler.source>
		<maven.compiler.target>21</maven.compiler.target>
	</properties>

	<modules>
		<module>calendar-service</module>
		<module>repository-service</module>
	</modules>

	<dependencies>

	</dependencies>

	<build>
		<plugins>

		</plugins>
	</build>

</project>

```

<br>

Microservicio Gestor de proyectos/calendario pom.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>app</groupId>
		<artifactId>microservices</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<artifactId>calendar-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>calendar-service</name>
	<description>API for sinaloa app with microservices</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.flywaydb</groupId>
			<artifactId>flyway-core</artifactId>
		</dependency>
		<dependency>
			<groupId>org.flywaydb</groupId>
			<artifactId>flyway-mysql</artifactId>
		</dependency>
		<dependency>
			<groupId>org.thymeleaf.extras</groupId>
			<artifactId>thymeleaf-extras-springsecurity6</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.sendgrid</groupId>
			<artifactId>sendgrid-java</artifactId>
			<version>4.10.1</version> <!-- Verifica la versión más reciente -->
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>

		<!--Dependencias JWT-->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.12.6</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.12.6</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
			<version>0.12.6</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.jetbrains</groupId>
			<artifactId>annotations</artifactId>
			<version>RELEASE</version>
			<scope>compile</scope>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.8.6</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```

<br>

Microservicio Repositorio de archivos: pom.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>app</groupId>
		<artifactId>microservices</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<artifactId>repository-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>repository-service</name>
	<description>API for sinaloa app with microservices</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.flywaydb</groupId>
			<artifactId>flyway-core</artifactId>
		</dependency>
		<dependency>
			<groupId>org.flywaydb</groupId>
			<artifactId>flyway-mysql</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</path>
					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>

```

<br>

## Application.properties

Solo crearemos este archivo para cada microservicio. La diferencia depende mucho de la base de datos a donde se conectaran, algunas variables y en especial el puerto donde se puede acceder
a cada microservicio. Para un despliegue es mejor trabajar con variables de entorno que valores escritos directamente. Eventualmente haremos dicho cambio


Gestor de proyectos properties con puerto 8082

```java
# ===== APPLICATION =====
spring.application.name=calendar-service

# ===== DATABASE (Variables de entorno) =====
spring.datasource.url=jdbc:mysql://localhost:3306/sinaloa_db
spring.datasource.username=demo
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===== JPA/HIBERNATE =====
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
#spring.jpa.hibernate.ddl-auto=${SPRING_JPA_DDL_AUTO:validate}

# ===== FLYWAY (Solo en produccion) =====
spring.flyway.enabled=true
spring.flyway.locations=classpath:/sql/migration
spring.flyway.clean-disabled=true


# ===== FRONTEND =====
sinaloa.frontend.url=http://localhost:8081

# ===== SERVER =====
server.port=8082
server.address=0.0.0.0

sinaloa.backend.urls.groups=http://localhost:8080/api/groups/

# ===== SENDGRID =====
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=${SENDGRID_API_KEY}
sendgrid.api.key=${SENDGRID_API_KEY}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

<br>

Repositorio properties con puerto 8080

```java
# ===== APPLICATION =====
spring.application.name=repository-service

# ===== DATABASE (Variables de entorno) =====
spring.datasource.url=jdbc:mysql://localhost:3306/files_db
spring.datasource.username=demo
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===== JPA/HIBERNATE =====
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
#spring.jpa.hibernate.ddl-auto=${SPRING_JPA_DDL_AUTO:validate}

# ===== FLYWAY (Solo en produccion) =====
spring.flyway.enabled=true
spring.flyway.locations=classpath:/sql/migration
spring.flyway.clean-disabled=true
#spring.flyway.user=${SPRING_DATASOURCE_USERNAME}
#spring.flyway.password=${SPRING_DATASOURCE_PASSWORD}

# ===== FILE UPLOAD =====
spring.servlet.multipart.max-file-size=15MB
spring.servlet.multipart.max-request-size=15MB
spring.servlet.multipart.resolve-lazily=true
sinaloa.repo.location=uploads


# ===== SERVER =====
server.port=8080
server.address=0.0.0.0
```

<br>

Una vez configurados dichos archivos, realizamos la implementación de servicios, controladores, etc.

## Ejecución y pruebas

Ejecutamos cada microservicio por separado desde su respectivo archivo MicroservicioApplication.java

0. Accedemos al primer microservicio y vemos que esta disponible (en el video hacemos mas pruebas interactuando con el cliente)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/50%20-%20microservice%20login.png" alt="login" width="700">

<br>

1. Mostramos todos los archivos existentes por grupo (GET)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/51%20-%20repo%20showall.png" alt="listar archivos por grupo" width="700">

<br>

2. Mostrar metadata de archivo por ID (GET)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/53%20-%20found.png" alt="archivo encontrado" width="700">

<br>

3. Archivo no encontrado con ID incorrecto (GET)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/52%20-%20not%20found.png" alt="not found" width="700">

<br>

4. Subimos un archivo (POST)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/54%20-%20upload.png" alt="subir archivo" width="700">

<br>

5. Eliminamos un archivo por ID (DELETE)
  
<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/55%20-%20delete.png" alt="eliminar archivo" width="700">

<br>

6. Descargamos un archivo por ID (GET)

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/957a1ccc548c0e39733c6614a498317f15213062/screenshots/56%20-%20download.png" alt="descargar archivo" width="700">

<br>

7. Algunos logs del microservicio de archivos

<br>

<img src="https://github.com/CristopherLodbrok117/migrating-monolito-to-microservices/blob/b64e9e27d35cf1dc55e3d273d8481578aea654ff/screenshots/57%20-%20some%20logs.png" alt="logs" width="700">

<br>

## Interacción desde cliente web
[Video de pruebas a API con microservicios](https://drive.google.com/file/d/1dp8Rr5ARJiOgUHr-1RkUtOldWTQ2nfNZ/view?usp=sharing)

