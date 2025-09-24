package com.example.geonotesteaching;

public record Link(String url, String label) implements Attachment {
    public Link {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Link.url requerido");
        }
        // Normalizamos label: si viene en blanco, lo tratamos como null
        if (label != null && label.isBlank()) {
            label = null;
        }
    }

    public String effectiveLabel() {
        return (label == null || label.isBlank()) ? url : label;
    }
}