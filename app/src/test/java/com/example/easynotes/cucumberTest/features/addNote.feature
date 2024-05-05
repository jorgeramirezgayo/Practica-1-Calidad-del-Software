Feature: Adding notes

  Scenario: Add a note and verify it is added
    Given a note with title "Título", color 0, text "Contenido", id 1, a relative path "path" and a list of tags
    When the note is added
    Then the note with id 0 should be added successfully
