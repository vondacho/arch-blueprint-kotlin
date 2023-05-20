Feature: To create one new client in a persistent way

  Background:
    Given user has write permission
    And a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: An existing client is a persisted resource in the system.

    Scenario: Add a new client to the existing clients
      Given the next identifier is afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc
      And the next timestamp is 2020-10-11T12:00:00
      When adding the new client
        | name | age |
        | test | 22  |
      Then the response status is CREATED
      And the attributes of the existing client afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc are the following
        | id                                   | name | age | creation-date       |
        | afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc | test | 22  | 2020-10-11T12:00:00 |

  Rule: An existing client has a valid set of attributes.

    Scenario Outline: Add a client with some invalid attributes
      Given the next identifier is afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc
      When adding the new client
        | name   | age   |
        | <name> | <age> |
      Then the response status is BAD_REQUEST
      And the set of existing clients is unchanged
      Examples:
        | name | age |
        |      |     |
        |      | 21  |

  Rule: An existing client is unique by name in the system.

    Scenario: Add a client duplicate on name when adding a new client
      Given the next identifier is afd9ce9f-ee0e-4547-8c77-3cc43ec85dbc
      And the next timestamp is 2020-10-11T12:00:00
      When adding the new client
        | name    | age |
        | client1 | 21  |
      Then the response status is BAD_REQUEST
      And the set of existing clients is unchanged

