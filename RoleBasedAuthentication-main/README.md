

This Spring Boot project demonstrates the implementation of Screen monitoring, a common approach to managing access control in applications. In role-based authentication, users are granted access based on predefined roles such as "USER" and "ADMIN".

## Key Components:

1. **Model:**
   - Contains Java classes representing entities in the application, defining the structure of data stored in the database and exchanged between components.
   
2. **Repository:**
   - Provides interfaces for interacting with the database, enabling CRUD operations on entities.
   
3. **Service:**
   - Contains business logic and services for user authentication, token management, and user details retrieval.
   
4. **Controller:**
   - Defines RESTful endpoints for user authentication and demo purposes.
   
5. **Filter:**
   - Spring Security filter responsible for intercepting requests, extracting JWT tokens, and authenticating users.
   
6. **Configuration:**
   - Configures Spring Security, defines authorization rules, and registers filters.

## How It Works:

1. **User Registration:**
   - New users can register using the `/auth/register` endpoint. The service encrypts passwords, saves user details to the database, generates JWT tokens, and returns them in the authentication response.
   
2. **User Authentication:**
   - Users authenticate via the `/auth/login` endpoint. Upon successful authentication, a new JWT token is generated, replacing existing tokens associated with the user.
   
3. **JWT Token Management:**
   - Handles JWT token generation, validation, and extraction of user details.
   
4. **Authorization and Access Control:**
   - Endpoints are protected based on user roles. Access to certain endpoints is restricted to users with specific roles.
   
5. **Token Revocation:**
   - When a user logs in, existing tokens are revoked, ensuring only the latest token remains valid.

## Setup Instructions:

1. **Database Configuration:**
   - Configure MySQL database settings in `application.properties`.
   
2. **Run the Application:**
   - Build and run the Spring Boot application.
   
3. **Testing:**
   - Test user registration, authentication, and access to secured endpoints.

## Prerequisites 
Before installing the application, ensure you have the following prerequisites installed:

- Java Development Kit (JDK) 11 or later
- Apache Maven
- MySQL or another compatible relational database


## Getting Started

To run the application locally, follow these steps:

1. Clone the repository:

```bash
 
```
2. Navigate to the project directory:

```bash
 cd RoleBasedAuthentication
```
3. Update the application.properties file with your MySQL database configuration.

```bash
 spring.datasource.url = jdbc:pgsql://localhost:8082/your_database_name
 spring.datasource.username = your_sql_username
 spring.datasource.password = your_sql_password
```

4. Build the application:
```bash
 mvn clean install
```

5. Run the application


6. Access the application at http://localhost:8082.

## Tech Stack

- Java
- Springboot
- PostgreSQL

## API Endpoints

1. **User Registration:**
   - **URL:** /auth/register
   - **Method:** POST
   - **Description:** Allows new users to register by providing their details.
   - **Request Body:** JSON object containing user details (firstName, lastName, username, password, role).
   - **Response:** Returns an AuthenticationResponse object containing a JWT token and a message indicating successful registration or an error message if the user already exists.

2. **User Login:**
   - **URL:** /auth/login
   - **Method:** POST
   - **Description:** Allows existing users to authenticate and obtain a JWT token for accessing protected endpoints.
   - **note:** copy token for logout.
   - **Request Body:** JSON object containing user credentials (username, password).
   - **Response:** Returns an AuthenticationResponse object containing a JWT token and a message indicating successful login or an error message if authentication fails.

3. ** USER Logout:**
   -**URL:** /auth/logout
   - **Description:** Use login token to Allows existing users to logout endpoints.
   - **Bearer Token:** paste the login token to logout.
   - **Response:** Returns an AuthenticationResponse object containing a JWT token and a message indicating successful logout .
   
4. **Session endpoint check**
   --http://localhost:8082/auth/debug/sessions?username=
   -**URL:** /auth/debug/sessions?username=
   - **Description:** Check user session are active.

5.  **Screenshot**
    --**URL:** /api/screenshots
    - **Description:** Allow user to record the active user time and duration and also save the screenshot picture id.


**END**

   



