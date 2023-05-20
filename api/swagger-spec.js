window.swaggerSpec={
  "openapi" : "3.0.3",
  "info" : {
    "title" : "Blueprint API",
    "description" : "This API allows managing Client entities.\n",
    "contact" : {
      "name" : "API Support"
    },
    "version" : "1.0.0",
    "x-audience" : "internal-public"
  },
  "servers" : [ {
    "url" : "http://localhost:8080"
  } ],
  "tags" : [ {
    "name" : "client",
    "description" : "Management of Client entity store"
  } ],
  "paths" : {
    "/clients" : {
      "get" : {
        "tags" : [ "client" ],
        "summary" : "List clients by criteria",
        "description" : "Lists existing clients given a set of matching filters",
        "operationId" : "listClients",
        "parameters" : [ {
          "name" : "name",
          "in" : "query",
          "description" : "term used as matching criteria for the name attribute",
          "required" : false,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "term",
          "in" : "query",
          "description" : "term used as matching criteria for any attribute",
          "required" : false,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "activatedOnly",
          "in" : "query",
          "description" : "result has to contain only activated clients",
          "required" : false,
          "schema" : {
            "type" : "boolean"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "successful operation",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "$ref" : "#/components/schemas/ClientOutput"
                  }
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      },
      "post" : {
        "tags" : [ "client" ],
        "summary" : "Add a new client to the store",
        "description" : "Add a new client to the store, a valid response contains a generated unique ID and the creation date\n",
        "operationId" : "addClient",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/ClientInput"
              }
            }
          },
          "required" : true
        },
        "responses" : {
          "201" : {
            "description" : "successful operation",
            "content" : {
              "text/plain" : {
                "schema" : {
                  "type" : "string",
                  "format" : "uuid"
                }
              }
            }
          },
          "400" : {
            "description" : "Bad request",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      }
    },
    "/clients/{id}" : {
      "get" : {
        "tags" : [ "client" ],
        "summary" : "Get client by ID",
        "description" : "Returns a single client",
        "operationId" : "getClientById",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "ID of client to return",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "uuid"
          }
        }, {
          "name" : "activatedOnly",
          "in" : "query",
          "description" : "consider activated clients only",
          "required" : false,
          "schema" : {
            "type" : "boolean"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "successful operation",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ClientOutput"
                }
              }
            }
          },
          "400" : {
            "description" : "Bad request",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "404" : {
            "description" : "Client not found",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      },
      "put" : {
        "tags" : [ "client" ],
        "summary" : "Replace an existing client",
        "description" : "Replace a single client",
        "operationId" : "replaceClient",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "Client id to replace",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "uuid"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/ClientInput"
              }
            }
          },
          "required" : true
        },
        "responses" : {
          "204" : {
            "description" : "successful operation"
          },
          "400" : {
            "description" : "Bad request",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "404" : {
            "description" : "Client not found",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      },
      "delete" : {
        "tags" : [ "client" ],
        "summary" : "Removes a client",
        "description" : "Removes a single client",
        "operationId" : "removeClient",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "Client id to remove",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "uuid"
          }
        } ],
        "responses" : {
          "204" : {
            "description" : "successful operation"
          },
          "400" : {
            "description" : "Client cannot be removed",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "404" : {
            "description" : "Client not found",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      }
    },
    "/clients/{id}:restore" : {
      "post" : {
        "tags" : [ "client" ],
        "summary" : "Restores a removed client",
        "description" : "Restores a single removed client",
        "operationId" : "restoreClient",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "Client id to restore",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "uuid"
          }
        } ],
        "responses" : {
          "204" : {
            "description" : "successful operation"
          },
          "400" : {
            "description" : "Client cannot be restored",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "404" : {
            "description" : "Client not found",
            "content" : {
              "application/problem+json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/ProblemOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      }
    },
    "/clients:search" : {
      "get" : {
        "tags" : [ "client" ],
        "summary" : "List clients given a term",
        "description" : "Lists clients which any field matches the given a term",
        "operationId" : "search",
        "parameters" : [ {
          "name" : "term",
          "in" : "query",
          "description" : "term used as matching criteria for any attribute",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "activatedOnly",
          "in" : "query",
          "description" : "consider activated clients only",
          "required" : false,
          "schema" : {
            "type" : "boolean"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "successful operation",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/SearchOutput"
                }
              }
            }
          },
          "401" : {
            "$ref" : "#/components/responses/UnauthorizedError"
          }
        },
        "security" : [ {
          "basicAuth" : [ ]
        } ]
      }
    }
  },
  "components" : {
    "schemas" : {
      "SearchOutput" : {
        "type" : "object",
        "properties" : {
          "clients" : {
            "type" : "array",
            "items" : {
              "$ref" : "#/components/schemas/ClientOutput"
            }
          }
        }
      },
      "ClientInput" : {
        "type" : "object",
        "properties" : {
          "name" : {
            "type" : "string"
          },
          "age" : {
            "type" : "number",
            "minimum" : 0
          }
        },
        "required" : [ "name" ]
      },
      "ClientOutput" : {
        "type" : "object",
        "properties" : {
          "id" : {
            "type" : "string",
            "format" : "uuid"
          },
          "name" : {
            "type" : "string"
          },
          "age" : {
            "type" : "number",
            "minimum" : 0
          },
          "createdAt" : {
            "type" : "string",
            "format" : "datetime"
          },
          "activated" : {
            "type" : "boolean"
          }
        },
        "required" : [ "id", "name", "createdAt" ]
      },
      "ProblemOutput" : {
        "type" : "object",
        "properties" : {
          "title" : {
            "type" : "string",
            "description" : "The error title.",
            "example" : "Bad Request"
          },
          "status" : {
            "type" : "integer",
            "description" : "The HTTP status.",
            "format" : "int32",
            "example" : 400
          },
          "detail" : {
            "type" : "string",
            "description" : "Detailed information about the invalid request.",
            "example" : "'name' attribute is expected."
          }
        },
        "required" : [ "title", "status", "detail" ]
      }
    },
    "responses" : {
      "UnauthorizedError" : {
        "description" : "Authentication information is missing or invalid",
        "headers" : {
          "WWW_Authenticate" : {
            "schema" : {
              "type" : "string"
            }
          }
        }
      }
    },
    "securitySchemes" : {
      "basicAuth" : {
        "type" : "http",
        "scheme" : "basic"
      }
    }
  },
  "security" : [ {
    "basicAuth" : [ ]
  } ]
}