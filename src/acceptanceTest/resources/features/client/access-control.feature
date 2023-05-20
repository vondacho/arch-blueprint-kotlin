Feature: To allow only legitimate users to read and write client data

  Background:
    Given a following set of existing clients
      | model-id | id                                   | name    | age | creation-date       |
      | 1        | ce751f30-217a-422c-b81b-8f75df4917b6 | client1 | 21  | 2020-10-10T12:00:00 |

  Rule: Only authenticated users who have the read permission can read data

    Scenario: A user must be authenticated to read
      Given user is not authenticated
      When looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is UNAUTHORIZED

    Scenario: A user with no permission cannot read
      Given user has no permission
      When looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is FORBIDDEN

    Scenario: An authorized user can read
      Given user has read permission
      When looking up existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is OK

  Rule: Only authenticated users who have the write permission can write data

    Scenario: A user must be authenticated to create
      Given user is not authenticated
      When adding the new client
        | first-name | last-name |
        | bob        | dylan     |
      Then the response status is UNAUTHORIZED

    Scenario: A user with no permission cannot create
      Given user has no permission
      When adding the new client
        | first-name | last-name |
        | bob        | dylan     |
      Then the response status is FORBIDDEN

    Scenario: A user must be authenticated to modify
      Given user is not authenticated
      When replacing existing client ce751f30-217a-422c-b81b-8f75df4917b6
        | first-name | last-name |
        | elvis      | presley   |
      Then the response status is UNAUTHORIZED

    Scenario: A user with no permission cannot modify
      Given user has no permission
      When replacing existing client ce751f30-217a-422c-b81b-8f75df4917b6
        | first-name | last-name |
        | elvis      | presley   |
      Then the response status is FORBIDDEN

    Scenario: A user must be authenticated to remove
      Given user is not authenticated
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is UNAUTHORIZED

    Scenario: A user with no permission cannot remove
      Given user has no permission
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is FORBIDDEN

    Scenario: A user with write permission cannot remove
      Given user has write permission
      When removing existing client ce751f30-217a-422c-b81b-8f75df4917b6
      Then the response status is FORBIDDEN
