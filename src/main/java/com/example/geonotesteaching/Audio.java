package com.example.geonotesteaching;

public record Audio(String url, int duration) implements Attachment {
    public Audio {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Audio.url requerido");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("Audio.duration no puede ser negativo: " + duration);
        }
    }
}
