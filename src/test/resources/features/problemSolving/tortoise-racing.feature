Feature: To calculates the time (hours, minutes, seconds) needed by a car B to catch another car A
  # See https://www.codewars.com/kata/tortoise-racing
  # See https://bitbucket.org/socraagile/tortoise_racing_car/src/elixir-olivier/

  Scenario: A first invariant says that B cannot reach A with same speeds and positive lead.

    Given a vehicle A with speed of 1 foot per hour
    And a vehicle B with speed of 1 foot per hour
    And vehicle A is leading by 1 foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is undefined

  Scenario: A next invariant says that B cannot reach A if A is quicker.

    Given a vehicle A with speed of 2 foot per hour
    And a vehicle B with speed of 1 foot per hour
    And vehicle A is leading
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is undefined

  Scenario: A next invariant says that B is able to reach A in one hour if speed difference is one foot and lead is one foot.

    Given a vehicle A with speed of 1 foot per hour
    And a vehicle B is quicker than A by 1 foot per hour
    And vehicle A is leading by 1 foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is 1 hour.s

  Scenario Outline: A next invariant says that more generally B is able to reach A in g hours if the difference of speed is one foot and lead is g feet.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B is quicker than A by 1 foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <hours> hour.s
    Examples:
      | speed-A | lead | hours |
      | 1       | 1    | 1     |
      | 2       | 2    | 2     |
      | 3       | 3    | 3     |

  Scenario Outline: A next invariant says that B is able to reach A in one hour if the lead equals to the difference of speed.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B is quicker than A by <speed-diff> foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <hours> hour.s
    Examples:
      | speed-A | speed-diff | lead | hours |
      | 1       | 1          | 1    | 1     |
      | 2       | 2          | 2    | 1     |
      | 3       | 3          | 3    | 1     |

  Scenario Outline: A next invariant says that B is able to reach A in k hours if the difference of speed is a divisor of the lead, the factor being k.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B is quicker than A by <speed-diff> foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <hours> hour.s
    Examples:
      | speed-A | speed-diff | lead | hours |
      | 10      | 2          | 10   | 5     |
      | 10      | 3          | 21   | 7     |
      | 10      | 5          | 30   | 6     |

  Scenario Outline: A next invariant says that B is able to reach A in k minutes if the difference of speed is the product of 60 by a given factor n, and the lead is the product of k by n.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B is quicker than A by <speed-diff> foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <minutes> minute.s
    Examples:
      | speed-A | speed-diff | lead | minutes |
      | 1       | 60         | 1    | 1       |
      | 1       | 120        | 4    | 2       |
      | 1       | 180        | 8    | 2       |

  Scenario Outline: A next invariant says that B is able to reach A in k seconds if the difference of speed is the product of 3600 by a given factor n, and the lead is the product of k by n.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B is quicker than A by <speed-diff> foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <seconds> second.s
    Examples:
      | speed-A | speed-diff | lead | seconds |
      | 1       | 3600       | 1    | 1       |
      | 1       | 7200       | 4    | 2       |
      | 1       | 10800      | 8    | 2       |

  Scenario Outline: The set of invariants being complete, this last scenario proposes to run specific test samples.

    Given a vehicle A with speed of <speed-A> foot per hour
    And a vehicle B with speed of <speed-B> foot per hour
    And vehicle A is leading by <lead> foot
    When calculating the time needed by vehicle B to catch vehicle A
    Then the resulting time is <hours> hour.s <minutes> minute.s <seconds> second.s
    Examples:
      | speed-A | speed-B | lead | hours | minutes | seconds |
      | 720     | 850     | 70   | 0     | 32      | 18      |
      | 80      | 91      | 37   | 3     | 21      | 49      |
