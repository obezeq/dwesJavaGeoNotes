package com.example.geonotesteaching.model;

import com.example.geonotesteaching.Video;

// Una 'sealed interface' permite controlar qué clases o records pueden implementarla.
// Esto es útil para modelar jerarquías cerradas y seguras.
public sealed interface Attachment permits Photo, Audio, Link, Video {
}