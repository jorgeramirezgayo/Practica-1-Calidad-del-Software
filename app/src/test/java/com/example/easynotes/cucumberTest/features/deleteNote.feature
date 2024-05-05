Feature: Delete Note
  Scenario: Deleting a    Given a note with title "Título", color 0, text "Contenido", id 1, a relative path "path" and an empty note
 list of tags
    When the note is deleted
    Then the note with id 1 should not exist


