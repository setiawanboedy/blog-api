# API Spec

## Authentication

## Login

Request :
- Method : POST
- Endpoint : `/api/auth/login`
- Header :
    - Content-Type: application/json
    - Accept: application/json
- Body :

```json
{
    "username" : "string|email, unique",
    "password" : "string"
}
```

- Response :

```json
{
    "status": "string",
    "code": "number",
    "message": "string",
    "data": {
        "token": "string, unique",
        "expires_in": "number",
    }
}
```

## Register

Request :
- Method : POST
- Endpoint : `/api/auth/register`
- Header :
    - Content-Type: application/json
    - Accept: application/json
- Body :

```json
{
    "username":"string, unique",
    "email":"string, unique",
    "password":"string"
}
```

- Response :

```json
{
    "status": "string",
    "code": "number",
    "message": "string",
    "data": {
        "id": "number, unique",
        "username": "string, unique",
        "email": "string, unique",
        "phoneNumber": "string",
        "address": "string",
        "profilePicture": "string"
    }
}
```