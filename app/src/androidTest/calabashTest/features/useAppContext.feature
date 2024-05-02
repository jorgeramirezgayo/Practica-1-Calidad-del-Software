Feature: Use App Context feature

  Scenario: As a valid user I can use the app context
    When I press "EasyNotes"
    Given I see "Easy Notes"
    Then I see "The Menu"
