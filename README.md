# API Spec

## Authentication

## login

Request :
- Method : POST
- Endpoint : `/api/users/me`
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
        "id": "number, unique",
        "username": "string, unique",
        "email": "string, unique",
        "phoneNumber": "string",
        "address": "string",
        "profilePicture": "string"
    }
}
```