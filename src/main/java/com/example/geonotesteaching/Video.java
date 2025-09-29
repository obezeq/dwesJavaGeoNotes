package com.example.geonotesteaching;

import com.example.geonotesteaching.model.Attachment;

public record Video(String url, int width, int height, int seconds) implements Attachment {

}