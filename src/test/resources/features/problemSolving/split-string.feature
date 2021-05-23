Feature: To split a text into smaller sections of equal length
  # https://bitbucket.org/socraagile/split-string

  Scenario Outline: To split a given text using a comma separator and optional underscore terminators

    When splitting <text> with an amplitude of <amplitude>
    Then the result of splitting is <result>
    Examples:
      | text   | amplitude | result   |
      | -      | 1         | -        |
      | a      | 1         | a        |
      | ab     | 1         | a,b      |
      | a      | 2         | a_       |
      | ab     | 2         | ab       |
      | abc    | 2         | ab,c_    |
      | abcd   | 2         | ab,cd    |
      | a      | 3         | a__      |
      | ab     | 3         | ab_      |
      | abc    | 3         | abc      |
      | abcd   | 3         | abc,d__  |
      | abcde  | 3         | abc,de_  |
      | abcdef | 3         | abc,def  |
