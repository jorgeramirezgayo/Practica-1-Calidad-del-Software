Feature: Delete Note
  Scenario:  User deletes an existing note and it should no longer exist
    Given an existing note with title "Título", color 0, text "Contenido", id 1, a relative path "path" and an empty list of tags
    When the note is deleted
    Then the note with id 1 should not exist


