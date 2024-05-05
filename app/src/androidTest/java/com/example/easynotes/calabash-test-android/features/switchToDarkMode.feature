Feature: Dark mode feature

  Scenario:  As a valid user I can switch to dark mode of my app
    Given I am in the "Settings" view
    When I press the "Modo Oscuro" radio button
    Then I see the mode go dark