package com.example.geonotesteaching;

// Un 'record' que usa otros 'records' para definir un área geográfica.
public record GeoArea(GeoPoint topLeft, GeoPoint bottomRight) { }