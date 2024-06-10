# Generator API spec
## SELECT QL
Endpoint : GET /api/favorite

Description : When QL is generated, there is an alert confirm for saved this query set format or no 
users need named the favorit and can be re usable format for the other data excel

Request Header : none

## Request Body :

```json
{ 
  "nameQL": "favorite",
  "opertaions": "SELECT",
}
```
## Response Body 
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

# SAVED QL
Endpoint : POST /api/favorite
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
    "success":"string"    
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