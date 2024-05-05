Feature: Edit Note

  Scenario: Editing an existing note
    Given an edited note with title "Título", color 0, text "Contenido", id 1, a relative path "path" and an empty list of tags
    When the note is saved
    Then the retrieved note with id 1 should have changed