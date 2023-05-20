Feature: To remove any persisted existing client

  Background:
    Given user has remove permission
    And a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: Only existing clients can be removed

    Scenario: A removed client cannot be looked up
      Given the next timestamp is 2020-10-11T12:00:00
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is NOT_FOUND

    Scenario: A removed client cannot be searched
      Given the next timestamp is 2020-10-11T12:00:00
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing clients with name client1
      Then the response status is OK
      And the result set of existing clients is empty

  Rule: Non-existing clients cannot be removed.

    Scenario: Remove a non-existing client
      When removing existing client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND
