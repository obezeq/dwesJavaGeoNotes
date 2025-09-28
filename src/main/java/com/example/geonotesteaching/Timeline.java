package com.example.geonotesteaching;

import java.util.*;
import java.util.stream.Collectors;
// La clase 'Timeline' usa un 'SequencedMap' para mantener las notas en orden de inserción.
// A diferencia de un HashMap, un 'SequencedMap' garantiza el orden y permite acceder
// al primer y último elemento de forma eficiente.
final class Timeline {
    private final Map<Long, Note> notes = new LinkedHashMap<>();

    public void addNote(Note note) { notes.put(note.id(), note); }
    public Note getNote(long id) { return notes.get(id); }
    public Map<Long, Note> getNotes() { return notes; }

    // Esta clase final genera la salida JSON usando 'text blocks'.
    public final class Render extends AbstractExporter implements Exporter {
        @Override
        public String export() {
            var notesList = notes.values().stream()
                    .map(note -> {
                        String escapedTitle = note.title().replace("\"", "\\\"");
                        String escapedContent = note.content().replace("\"", "\\\"");
                        return """
                        {
                          "id": %d,
                          "title": "%s",
                          "content": "%s",
                          "location": {
                            "lat": %.5f,
                            "lon": %.5f
                          },
                          "createdAt": "%s"
                        }
                        """.formatted(
                                note.id(), escapedTitle, escapedContent,
                                note.location().lat(), note.location().lon(),
                                note.createdAt()
                        );
                    })
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.joining(",\n"));

            return """
                {
                  "notes": [
                    %s
                  ]
                }
                """.formatted(notesList);
        }
    }

    public java.util.List<String> searchName(String input) {
        List<String> results = new ArrayList<>();
        for (Note note : getNotes().values()) {
            if (note.title().contains(input) || note.content().contains(input)) {
                results.add(String.format("ID:%d | Título: %s | Contenido: %s",
                        note.id(), note.title(), note.content()));
            }
        }
        return results;
    }

    public java.util.List<String> searchByArea(GeoArea area) {
        java.util.List<String> results = new java.util.ArrayList<>();

        for (Note note : getNotes().values()) {
            if (Match.isInArea(note.location(), area)) {
                results.add(String.format(
                        "ID:%d | Título: %s | Contenido: %s ",
                        note.id(), note.title(), note.content()));
            }
        }

        return results;
    }

    public java.util.List<String> latest(int n){
        var allNotes = new ArrayList<>(getNotes().values());
        if (n > allNotes.size()) n = allNotes.size();
        if (n <= 0) return new ArrayList<>();
        return allNotes.subList(allNotes.size() - n, allNotes.size())
                .stream()
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .map(note -> String.format("ID:%d | Título: %s | Contenido: %s",
                        note.id(), note.title(), note.content()))
                .collect(Collectors.toList());
    }
}