# Ensportive Backend

This is the backend application for the Ensportive project, built using Java with Spring Boot.

## Prerequisites

Before installing and running this project, ensure you have the following prerequisites:

1. **Java**: Make sure you have Java installed on your system. You can download and install Java from [here](https://www.oracle.com/br/java/technologies/downloads/#jdk21-windows).

2. **Maven**: Maven is used as a build automation tool for Java projects. Make sure you have Maven installed. You can download and install Maven from [here](https://maven.apache.org/download.cgi).

3. **Docker Desktop**: Ensure you have Docker Desktop installed on your system. Docker Desktop provides an easy-to-install application for Docker Engine, Kubernetes, and other tools. You can download and install Docker Desktop from [here](https://www.docker.com/products/docker-desktop).

## Installation

1. Clone the repository:
    ```bash
    git clone https://tools.ages.pucrs.br/ensportive/ensportive-backend.git
    ```

2. Navigate to the project directory:
    ```bash
    cd ensportive-backend
    ```

3. Build the project using Maven:
    ```bash
    mvn clean install
    ```

## Usage

1. Create the Docker volume (only need to be done the first time):
    ```bash
    docker volume create ensportive-db
    ```

2. Run the Docker compose file:
    ```bash
    docker compose up
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

   This will start the backend server.

4. The backend server will be accessible at [http://localhost:8080](http://localhost:8080).