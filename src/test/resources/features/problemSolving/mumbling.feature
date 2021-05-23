Feature: To mumble the letters of a text
  # See https://learn.madetech.com/katas/mumbling/
  # See https://bitbucket.org/noiasquad/mumbling_kata_elixir

  Scenario Outline: Split a given text, repeating the letters, and capitalizing the first occurrence only

    When mumbling <text>
    Then the result of mumbling is <result>
    Examples:
      | text   | result                     |
      | -      | -                          |
      | A      | A                          |
      | a      | A                          |
      | ab     | A-Bb                       |
      | abc    | A-Bb-Ccc                   |
      | abC    | A-Bb-Ccc                   |
      | aBCd   | A-Bb-Ccc-Dddd              |
      | QWERTY | Q-Ww-Eee-Rrrr-Ttttt-Yyyyyy |
