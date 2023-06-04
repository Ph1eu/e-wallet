# E-Wallet

This repository contains the source code and related files for an E-Wallet application. The application is built using the Spring family of frameworks, Node.js, and Kafka messaging system.

## Table of contents
- Overview
- Tech stack
- Installation
- Usage

## Overview

## Tech Stack 
The E-Wallet microservice-based application leverages the following technologies and frameworks:

- Spring Family: The Spring framework provides a comprehensive ecosystem for developing enterprise-grade applications. In this project, the following Spring modules are used:

  - Spring Boot: Spring Boot simplifies the setup and configuration of Spring applications, providing a robust foundation for building backend APIs. It offers features such as auto-configuration, embedded server deployment, and production-ready defaults.

  - Spring Security: Spring Security is a powerful framework for implementing authentication and authorization in Java applications. It provides various mechanisms for securing endpoints, managing user roles and permissions, and integrating with different authentication providers.

- Node.js : Node.js is a runtime environment for executing JavaScript code outside the web browser. In this project, Node.js is used to develop a microservice responsible for documenting the API using Swagger. Swagger is a powerful tool for designing, building, and documenting RESTful APIs.

- Kafka: Kafka is a distributed streaming platform that enables the building of real-time data pipelines and streaming applications. It provides a highly scalable and fault-tolerant messaging system for communication between microservices. In the E-Wallet application, Kafka is specifically used for facilitating transactions between users. It ensures reliable and asynchronous communication for seamless transaction processing.


## Installation
To set up the E-Wallet application locally, follow these steps:
1. Clone the repository:
```
git clone https://github.com/your-username/e-wallet.git
```
2. Install the required dependencies:
- For Spring-based microservices:
  - Make sure you have Maven installed on your system. Follow the official Maven installation guide for your operating system: Maven Installation

  - Navigate to the directory of each Spring-based microservice.

  - Open the terminal and run the following command to install the project dependencies using Maven
    ```bash 
    mvn clean install
    ```
  - Maven will read the `pom.xml` file in each microservice's directory and download the required dependencies.
- For the Node.js microservice responsible for Swagger documentation:

    - Navigate to the relevant directory for the Node.js microservice responsible for Swagger documentation.

    - Open the terminal and run the following command to install the project dependencies using npm:
    
    ```bash
    npm install
    ```
3. Configure the microservices:

- Modify the necessary configuration files for each microservice, such as `application.properties` or `application.yml`, to specify settings like database connection details, Kafka broker configuration, authentication and authorization rules in Spring Security, and any other environment-specific variables required by each microservice.

4. Start the microservices:

- For Spring-based microservices:

    - Navigate to the root directory of each Spring-based microservice in the terminal.

  - Run the following command to start the microservice using Maven:
    ```
    mvn spring-boot:run
    ```
  - Maven will compile the source code, resolve dependencies, and start the microservice.

- For the Node.js microservice responsible for Swagger documentation:

  - Navigate to the relevant directory for the Node.js microservice responsible for Swagger documentation.

  - Run the following command to start the microservice:

  ```bash
  npm start
  ```