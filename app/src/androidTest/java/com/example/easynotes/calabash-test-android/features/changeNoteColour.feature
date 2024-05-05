Feature: Change Note colour feature

  Scenario:  As a valid user I can change the colour of my note
    Given I am in the "Edit Note" view
    When I press the "Azul" radio button
    And I press the "Save" button
    Then I see the note change colour