package com.example.geonotesteaching;

import com.example.geonotesteaching.model.Attachment;
import com.example.geonotesteaching.model.Audio;
import com.example.geonotesteaching.model.Link;
import com.example.geonotesteaching.model.Photo;

// Esta clase usa 'switch expressions' y 'pattern matching' para describir un 'Attachment'.
// Los 'switch expressions' permiten que el 'switch' sea una expresión que devuelve un valor.
// El 'pattern matching' en el 'case' permite desestructurar el objeto y
// aplicar una condición ('when') de forma concisa.
final class Describe {
    public static String describeAttachment(Attachment a) {
        return switch (a) {
            case Photo p when p.width() > 1920 -> "📷 Foto en alta definición (%d x %d)".formatted(p.width(), p.height());
            case Photo p -> "📷 Foto";
            case Audio audio when audio.duration() > 300 -> {
                var mins = audio.duration() / 60;
                yield "🎵 Audio (" + mins + " min)";
            }
            case Audio audio -> "🎵 Audio";
            case Link l -> "🔗 %s".formatted((l.label() == null || l.label().isEmpty()) ? l.url() : l.label());
            case Video v when v.seconds() > 120 -> "  Vídeo largo";
            case Video v -> "  Vídeo";
        };
    }
    public static int mediaPixels(Object o) {
        if (o instanceof Photo p) {
            return p.width() * p.height();
        } else if (o instanceof Video v) {
            return v.width() * v.height();
        } else {
            return 0;
        }
    }
}