Feature: Restaurant Add Rating Behavior
  As a restaurant owner, I want reviews to be added to a restaurant

  Scenario: Getting a Review
    Given review added for the first time by user
    When review is valid
    Then restaurant gets the review

  Scenario: Getting a Second Review from User
    Given user added review before
    When user adds second review
    Then restaurant only keeps second review
