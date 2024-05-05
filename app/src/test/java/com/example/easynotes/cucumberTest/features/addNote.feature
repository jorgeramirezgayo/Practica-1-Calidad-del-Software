Feature: Adding notes

  Scenario:  User adds a new note and it should be shown
    Given a note with title "Título", color 0, text "Contenido", id 1, a relative path "path" and a list of tags
    When the note is added
    Then the note with id 0 should be retrieved successfully
