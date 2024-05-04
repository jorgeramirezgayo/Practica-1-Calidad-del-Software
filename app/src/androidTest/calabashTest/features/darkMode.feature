Feature: Dark Mode

  Scenario: User switches to dark mode from settings
    Given the user is in the application settings
    When the user taps on the "Dark Mode" option
    Then the application's color scheme changes to dark