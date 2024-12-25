Feature: Add Reservation Behavior
  As a user, I want to add reservations to my account so that I can book tables at a restaurant.

  Scenario: Add a valid reservation
    Given a client wants to make a reservation
    When the client makes a valid reservation
    Then the reservation should be added

  Scenario: Add an invalid reservation
    Given a client wants to make a reservation
    When the client makes an invalid reservation
    Then the reservation should not be added
