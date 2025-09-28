package com.example.geonotesteaching;

import java.util.stream.Collectors;

public final class MarkdownExporter implements Exporter {

    private final Timeline timeline;

    public MarkdownExporter(Timeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public String export() {
        var notesList = timeline.getNotes().values().stream()
                .map(note -> String.format(
                        "- [ID %d] %s - (%.5f, %.5f) - %s",
                        note.id(),
                        note.title(),
                        note.location().lat(),
                        note.location().lon(),
                        note.createdAt().toString().substring(0, 10)
                ))
                .collect(Collectors.joining("\n"));

        return """
                # GeoNotes
                %s
                """.formatted(notesList);
    }
}

