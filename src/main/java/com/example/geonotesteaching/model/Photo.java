package com.example.geonotesteaching.model;

// Los 'records' también pueden implementar interfaces. Son una forma limpia de
// definir los subtipos de la interfaz sellada.
public record Photo(String url, int width, int height) implements Attachment {
}