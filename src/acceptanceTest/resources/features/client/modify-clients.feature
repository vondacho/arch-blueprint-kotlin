Feature: To modify any persisted existing client

  Background:
    Given user has write permission
    And a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: An existing client is a persisted resource in the system.

    Scenario Outline: Apply a valid mutation to an existing client
      Given the next timestamp is 2020-10-11T12:00:00
      When replacing existing client <id>
        | name       | age       |
        | <new-name> | <new-age> |
      Then the response status is NO_CONTENT
      And the attributes of the existing client <id> are the following
        | id   | name       | age       | creation-date       |
        | <id> | <new-name> | <new-age> | 2020-10-10T12:00:00 |
      Examples:
        | id                                   | new-name | new-age |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1  | -       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1  | 21      |

  Rule: Only valid mutations can be applied to an existing client.

    Scenario Outline: Apply an invalid mutation to an existing client
      Given the next timestamp is 2020-10-11T12:00:00
      When replacing existing client <id>
        | name       | age       |
        | <new-name> | <new-age> |
      Then the response status is BAD_REQUEST
      And the attributes of the existing client <id> are unchanged
      Examples:
        | id                                   | new-name | new-age |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client2  | 21      |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | -        | 21      |

  Rule: Non-existing clients cannot be looked for nor updated nor deleted.

    Scenario: Update a non-existing client
      Given the next timestamp is 2020-10-11T12:00:00
      When replacing existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
        | name | age |
        | any  | 21  |
      Then the response status is NOT_FOUND

  Rule: An existing client is unique either by name in the system.

    Scenario: Add a client duplicate when updating an existing client
      Given the next timestamp is 2020-10-11T12:00:00
      When replacing existing client ce751f30-217a-422c-b81b-8f75df4917b6
        | name    | age |
        | client2 | 21  |
      Then the response status is BAD_REQUEST
      And the set of existing clients is unchanged
