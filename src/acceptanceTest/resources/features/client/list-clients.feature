Feature: To list any persisted existing client

  Background:
    Given user has read permission
    And a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: An existing client is a persisted resource in the system.

    Scenario: Look up an existing client
      When looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is OK
      And the attributes of the returned client are the following
        | id                                   | name    | age | creation-date       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |

    Scenario Outline: Search any existing clients
      When looking up existing clients with name <name-filter>
      Then the response status is OK
      And the result set of existing clients contains <res-ids>
      Examples:
        | name-filter | res-ids                                                                   |
        | -           | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | client      | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | ClIEnT      | ce751f30-217a-422c-b81b-8f75df4917b6,29e364b9-f5ef-43d9-9f30-e07a30b73e01 |
        | client1     | ce751f30-217a-422c-b81b-8f75df4917b6                                      |
        | x           | -                                                                         |

  Rule: Non-existing clients cannot be looked for nor updated nor deleted.

    Scenario: Look up a non-existing client
      When looking up existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND
