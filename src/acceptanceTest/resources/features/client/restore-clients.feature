Feature: To restore any persisted and deactivated existing client

  Background:
    Given user has remove permission
    And a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |
      | 2        | 29e364b9-f5ef-43d9-9f30-e07a30b73e01 | client2 | -   | 2020-10-09T12:00:00 |

  Rule: Removed clients can be restored and be looked up

    Scenario: Restore a removed client
      Given the next timestamp is 2020-10-11T12:00:00
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      And restoring client ce751f30-217a-422c-b81b-8f75df4917b6
      And looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is OK
      And the attributes of the returned client are the following
        | id                                   | name    | age | creation-date       |
        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |

  Rule: Non-removed clients cannot be restored.

    Scenario: Restore a non-removed client
      When restoring client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is NOT_FOUND

    Scenario: Restore a non-existing client
      When restoring client c9eda137-c57f-4ff9-b711-43ea5728ba9f
      Then the response status is NOT_FOUND
