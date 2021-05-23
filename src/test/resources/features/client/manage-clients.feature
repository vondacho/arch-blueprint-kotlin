Feature: To be able to manage a set of existing clients in a persistent way

  Background:
    Given a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: An existing client is a persisted resource in the system.

    Scenario: Add a new client to the existing clients

      Given a following set of client attributes
        | name | age |
        | test | 22  |
      And the next identifier is afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc
      And the next timestamp is 2020-10-11T12:00:00
      When registering the new client
      Then the response status is CREATED
      And the attributes of the returned client are the following
        | id                                   | name | age | creation-date       |
        | afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc | test | 22  | 2020-10-11T12:00:00 |
      And the returned client is added to the set of existing clients

    Scenario: Look up an existing client

      When looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is OK
      And the attributes of the returned client are the following
        | id                                   | name    | age | creation-date       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |

    Scenario Outline: Search any existing clients

      When looking up existing clients with name <name-filter>
      Then the response status is OK
      And the result set of existing clients is <res-ids>
      Examples:
        | name-filter | res-ids                                                                   |
        | -           | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | client      | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | ClIEnT      | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | client1     | ce751f30-217a-422c-b81b-8f75df4917b6                                      |
        | x           | -                                                                         |

    Scenario Outline: Apply a valid mutation to an existing client

      Given a following set of client attributes
        | name       | age       |
        | <new-name> | <new-age> |
      And the next timestamp is 2020-10-11T12:00:00
      When updating existing client <id>
      Then the response status is NO_CONTENT
      And the attributes of the existing client <id> are the following
        | id   | name       | age       | creation-date       |
        | <id> | <new-name> | <new-age> | 2020-10-10T12:00:00 |

      Examples:
        | id                                   | new-name | new-age |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1  | -       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1  | 21      |

  Rule: Only existing clients can be removed

    Scenario: A deleted client cannot be looked up

      When deleting existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is NOT_FOUND

    Scenario: A deleted client cannot be searched

      When deleting existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing clients with name client1
      Then the response status is OK
      And the result set of existing clients is -

  Rule: An existing client is unique either by identifier or by name in the system.

    Scenario: Add a client duplicate when adding a new client

      Given a following set of client attributes
        | name    | age |
        | client1 | 21  |
      And the next identifier is afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc
      And the next timestamp is 2020-10-11T12:00:00
      When registering the new client
      Then the response status is BAD_REQUEST
      And the set of existing clients is unchanged

    Scenario: Add a client duplicate when updating an existing client

      Given a following set of client attributes
        | name    | age |
        | client2 | 21  |
      And the next timestamp is 2020-10-11T12:00:00
      When updating existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is BAD_REQUEST
      And the set of existing clients is unchanged

  Rule: Only valid mutations can be applied to an existing client.

    Scenario Outline: Apply an invalid mutation to an existing client

      Given a following set of client attributes
        | name       | age       |
        | <new-name> | <new-age> |
      And the next timestamp is 2020-10-11T12:00:00
      When updating existing client <id>
      Then the response status is BAD_REQUEST
      And the attributes of the existing client <id> are unchanged

      Examples:
        | id                                   | new-name | new-age |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client2  | 21      |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | -        | 21      |

  Rule: Non-existing clients cannot be looked for nor updated nor deleted.

    Scenario: Look up a non-existing client

      When looking up existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND

    Scenario: Update a non-existing client

      Given a following set of client attributes
        | name | age |
        | any  | 21  |
      And the next timestamp is 2020-10-11T12:00:00
      When updating existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND

    Scenario: Delete a non-existing client

      When deleting existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND

  Rule: Deleted clients stay persistent and can be restored.

    Scenario: Restore a deleted client

      When deleting existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And restoring client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is OK
      And the attributes of the returned client are the following
        | id                                   | name    | age | creation-date       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |

    Scenario: Restore an existing client

      When restoring client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is NOT_FOUND
