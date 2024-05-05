Feature: Get notes

  Scenario:  User gets all existing notes and they should be shown
    Given two notes, one with "Título 1", color 1, text "Contenido 1", id 1, a relative path "path" and a list of tags and the other one with "Título 2", color 2, text "Contenido 2", id 2, a relative path "path" and a list of tags
    When all notes are retrieved with path "path" provided
    Then the note with id 1 and the note with the id 2 should be 2 in size as well as portrayed
