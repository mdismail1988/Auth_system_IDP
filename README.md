# Auth_system_IDP

## _API_

### Swagger: 

[Auth-Gateway-Service](http://localhost:9090/swagger-ui/index.html)
[IDP Service](http://localhost:9091/swagger-ui/index.html)

Auth-Gateway-Service ---------> IDP Service
Sample Cleint ID and Secret for testing: ndjtok37364 / 767867hjgsdsdh36h37dfdrwdhjkuytghfffdhsitdjedjd


### Authentication Gateway

| Method | Endpoint                              | Description                                |
|--------|---------------------------------------|--------------------------------------------|
| POST   | /users/register                       | Register user (delegates to IDP)           |
| GET    | /users/token                          | Fetch user details (delegates to IDP)      |
| POST   | /auth-gateway/auth/attempt            | Create an authentication attempt           |
| POST   | /auth-gateway/auth/accept/{attemptId} | Accept the attempt                         |
| POST   | /auth-gateway/auth/reject/{attemptId} | Reject the attempt                         |
| GET    | /auth-gateway/auth/status/{attemptId} | Check status of the authentication attempt |


### IDP Service

| Method | Endpoint                         | Description        |
|--------|----------------------------------|--------------------|
| POST   | api/idp-service/users/           |  Create new user   |
| GET    | api/idp-service/users//{email}   | Fetch user details |
| POST   | api/idp-service/users/token      | Issue JWT token    |


## **Local setup**
### Prerequisites
IntelliJ IDEA or any Java IDE
open JDK 21

1. Install Docker Desktop
    Download and install Docker Desktop from (https://www.docker.com/products/docker-desktop)
    Check Docker version with `docker --version` & Ensure Docker is running
2. IntelliJ & Docker Integration
    - Install the Docker plugin in IntelliJ IDEA.
    - Configure Docker in IntelliJ IDEA by going to `File -> Settings -> Plugins` and searching for "Docker".
3. Configure Docker in IntelliJ
    - Go to `File -> Settings -> Build, Execution, Deployment -> Docker`.
    - Click the "+" icon to add a new Docker configuration.
4. (or) Run using terminal (docker-compose)
    - Open a terminal and navigate to the project directory.
    - Run `docker-compose up --build` to start the services.
    - 'docker-compose up' --build -d to run in detached mode
    - Addtional Note : if build fails - 'mvn -N io.takari:maven:wrapper' & 'chmod +x mvnw' run this in each project directory to generate the maven wrapper

### Test with Postman - IDP Service

- choose Oauth 2.0
- select Configure New Token
- Enter the following details:
- Token Name: `AuthSystem`
- Grant Type: `Client Credentials`
- access Token URL: `http://localhost:9091/idp-service/users/token`
- Client ID: `ndjtok37364`
- Client Secret : `767867hjgsdsdh36h37dfdrwdhjkuytghfffdhsitdjedjd`
- Scope: `read write`
- Click on "Get New Access Token" to retrieve the token.
- Click on "Use Token" to set it for the requests.
- Test the following endpoints:
  - Register User: `POST http://localhost:9091/api/idp-service/users/users`
    - Body:
      {
      "username": "string",
      "email": "string"
      }


## **SYSTEM Architecture**

+---------------------+            +--------------------+
|    Client App       |            |     IDP Service    |
|  (Web/Mobile)       |            |                    |
+---------+-----------+            +----------+---------+
          |                                  ^
          | HTTP(s)                          | OAuth2 
          v                                  |
+---------+-----------+                      |
|  Authentication     |                      |
|  Gateway Service    |----------------------+
| (Token Issuer, API  |
|  Gateway, Validator)|
+----------------------+


### TO-DO
- Implement logging for all services.
- Set up monitoring for the services using tools like Prometheus and Grafana.
