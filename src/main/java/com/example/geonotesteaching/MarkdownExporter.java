package com.example.geonotesteaching;

import com.example.geonotesteaching.model.Attachment;
import com.example.geonotesteaching.model.Audio;
import com.example.geonotesteaching.model.Link;
import com.example.geonotesteaching.model.Photo;

import java.util.stream.Collectors;

public final class MarkdownExporter implements Exporter {

    private final Timeline timeline;

    public MarkdownExporter(Timeline timeline) {
        this.timeline = timeline;
    }

    @Override
    public String export() {
        var notesList = timeline.getNotes().values().stream()
                .map(note -> {

                    String attachmentText = note.attachment() != null ? " [" + formatAttachmentMarkdown(note.attachment()) + "]" : "";

                    return String.format(
                            "- [ID %d] %s - (%.5f, %.5f) - %s%s",
                            note.id(),
                            note.title(),
                            note.location().lat(),
                            note.location().lon(),
                            note.createdAt().toString().substring(0, 10),
                            attachmentText
                        );
                    })
                .collect(Collectors.joining("\n"));

        return """
                # GeoNotes
                %s
                """.formatted(notesList);
    }

    private String formatAttachmentMarkdown(Attachment attachment) {
        return switch (attachment) {

            case Photo p -> String.format("Foto %d x %d", p.width(), p.height());
            case Audio a -> String.format("Audio %ds", a.duration());

            case Link l -> {
                String displayText = (l.label() == null || l.label().isEmpty()) ? l.url() : l.label();
                yield String.format("Link %s", displayText);
            }

            case Video v -> String.format("Video %d x %d", v.width(), v.height());

        };
    }
}

