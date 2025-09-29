package com.example.geonotesteaching.model;

public record Video(String url, int width, int height, int seconds) implements Attachment {

}