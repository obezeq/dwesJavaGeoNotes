package com.example.geonotesteaching;

// Utilidades de “matching” y geolocalización.
// Un 'record' que contiene un método para usar 'record patterns'.
// El 'record pattern' permite desestructurar un record directamente en los parámetros
// de un método o en un 'if' o 'switch', lo que es muy útil para la validación y el filtrado.
final class Match {
    public static boolean isInArea(GeoPoint point, GeoArea area) {
        return point.lat() >= area.topLeft().lat() &&
                point.lat() <= area.bottomRight().lat() &&
                point.lon() >= area.topLeft().lon() &&
                point.lon() <= area.bottomRight().lon();
    }
    /**
     * Versión didáctica estilo “record patterns” (Java 21) para ubicar un punto en regiones lógicas.
     * Utiliza:
     * - Switch como expresión (devuelve un valor directamente).
     * - Record pattern: desestructura el record GeoPoint en lat y lon.
     * - Guardas (when): añaden condiciones adicionales a cada caso.
     */
    public static String where(GeoPoint p) {
        return switch (p) {
            // Caso ORIGIN: patrón record + guarda que comprueba si lat y lon son ambos 0
            case GeoPoint(double lat, double lon) when lat == 0 && lon == 0 -> "ORIGIN";

            // Caso Equator: latitud 0, cualquier longitud
            case GeoPoint(double lat, double lon) when lat == 0 -> "Equator";

            // Caso Greenwich: longitud 0, cualquier latitud
            case GeoPoint(double lat, double lon) when lon == 0 -> "Greenwich";

            // Caso general: cualquier otro punto, se devuelve como texto "(lat,lon)"
            case GeoPoint(double lat, double lon) -> "(" + lat + "," + lon + ")";
        };
    }
}