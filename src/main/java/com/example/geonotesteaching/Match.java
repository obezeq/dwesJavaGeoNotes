package com.example.geonotesteaching;
// Un 'record' que contiene un método para usar 'record patterns'.
// El 'record pattern' permite desestructurar un record directamente en los parámetros
// de un método o en un 'if' o 'switch', lo que es muy útil para la validación y el filtrado.
final class Match {
    public static boolean isInArea(GeoPoint p, GeoArea a) {
        return p.lat() >= a.topLeft().lat() && p.lat() <= a.bottomRight().lat() && p.lon() >= a.topLeft().lon() && p.lon() <= a.bottomRight().lon();
    }
}