Feature: Change text colour feature

  Scenario:  As a valid user I can change the colour of the text in my note
    Given I am in the "Edit Note" view
    When I press the "Gris" radio button
    And I press the "Save" button
    Then I see the text of my note change colour