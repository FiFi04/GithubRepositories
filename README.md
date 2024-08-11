# GithubRepositoriesApp

## Overview
Project uses GitHub Api to get all repositories of a given user, which are not forks.

## Requirements
- Java 21
- Spring Boot 3
- Maven

## Building
To build the project set the project directory and execute:
```bash
mvn clean install
```

## Running
To run the project execute:
```bash
mvn spring-boot:run
```

## Usage
To get repositories for GitHub user:
```bash
GET /api/github/repositories/{username}
Accept: application/json
```
Set {username} with GitHub username

## Error Handling
Format of the error for non-existing user, non-supported data type:

```json
{
"status": "<HTTP_STATUS_CODE>",
"Message": "<ERROR_MESSAGE>"
}
```