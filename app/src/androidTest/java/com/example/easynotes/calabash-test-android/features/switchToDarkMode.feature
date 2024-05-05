Feature: Dark mode feature

  Scenario: As a valid user I can switch to dark mode of my app
    Given I am on the "LightSettings" Screen
    When I touch the "Modo Oscuro" button
    Then I should see  "DarkSettings" Screen
    And I take a screenshot
