package com.example.geonotesteaching;
// Un 'record' que contiene un método para usar 'record patterns'.
// El 'record pattern' permite desestructurar un record directamente en los parámetros
// de un método o en un 'if' o 'switch', lo que es muy útil para la validación y el filtrado.
final class Match {
    public static boolean isInArea(GeoPoint p, GeoArea a) {
        return p.lat() >= a.topLeft().lat() && p.lat() <= a.bottomRight().lat() && p.lon() >= a.topLeft().lon() && p.lon() <= a.bottomRight().lon();
    }
    public static String where(GeoPoint p) {
        return switch (p) {
            case GeoPoint(double lat, double lon) when lat == 0 && lon == 0 -> "ORIGIN";
            case GeoPoint(double lat, double lon) when lat == 0 -> "Equator";
            case GeoPoint(double lat, double lon) when lon == 0 -> "Greenwich";
            case GeoPoint(double lat, double lon) -> "(" + lat + "," + lon + ")";
        };
    }
}