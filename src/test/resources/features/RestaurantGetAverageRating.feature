Feature: Get Average Rating Behavior
  As a restaurant owner, I want to get Average Rating of customer reviews

  Scenario: Getting Average of Ratings
    Given some clients submitted reviews
    When the reviews are valid
    Then average should be correct
