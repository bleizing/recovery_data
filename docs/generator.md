# Generator API spec
## Generate SELECT DELETE
Endpoint : POST /api/{operations}

Description : parameter in path url can fill what is operations sql needed like select update or deleted

Request Header : TOKEN

Request Body :

```json
{ 
  "regions": "uq_jp_db",
  "tables": "orders",
  "conditions" :{
    "conditions1" : {
      "columns": "A1",     //excel cell reference for disred which column want to SELECT for WHERE conditons
      "comparative":  "=",
      "values":"A2:A11"
    },
    "conditions2" : {
      "columns": "B2",     //excel cell reference for disred which column want to SELECT for WHERE need additional onditions
      "comparative":  "=",
      "values":"B2:B11"
    }
  }
}
```
## Response Body 
 ```json
 {
  "generateSQL":"SELECT ***********"
 }
 ```
 Response 400 Bad Request
 ```json
 {
  "error":"string"
 }
 ```
Response 500 Internal Server error
 ```json
 {
  "error":"string"
 }
 ```

## Generate UPDATE AND SELECT

Endpoint : POST /api/{operations}

Description : parameter in path url can fill what is operations sql needed like select update or deleted

Request Header : none

Request Body :

```json
{ 
  "regions": "uq_jp_db",
  "tables": "orders",
  "sets" : {
    "set1": {
      "collumns":"referenceCell(C2) / set manual",
      "value" : "referenceCell(D2)",
      },
    "set2": {
      "collumns":"referenceCell(C2) / set manual",
      "value" : "referenceCell(D2)",
      },
    }
  "conditions" :{
    "conditions1" : {
      "columns": "A1",     //excel cell reference for disred which column want to SELECT for WHERE conditons
      "params":  "=",
      "values":"A2:A11"
    },
    "conditions2" : {
      "columns": "B2",     //excel cell reference for disred which column want to SELECT for WHERE need additional conditions
      "params":  "=",
      "values":"B2:B11"
    }
  }
}
```
## Response Body 
 ```json
 {
  "generateSQL":"SELECT ***********"
 }
 ```
 Response 400 Bad Request
 ```json
 {
  "error":"string"
 }
 ```
Response 500 Internal Server error
 ```json
 {
  "error":"string"
 }
 ```

