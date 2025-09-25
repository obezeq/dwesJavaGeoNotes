package com.example.geonotesteaching;

/*
*
* ¿CUÁNDO Y CUÁNDO NO USAR RECORD?
*
* - Cuando lo único que necesitamos es almacenar datos en una estructura y incluso hacer validaciones si lo requerimos,
*   realmente el record es lo único que necesitamos en vez de crear la clase manualmente, debido a que también java
*   te crea el hashCode, equals y toString de forma automática.
*
* - Si necesitamos añadir mas funcionalidades al record, a parte de la validación básica, y el constructor automático,
*   entonces ahí ya deberíamos utilizar una clase clásica.
*
* */

public class LegacyPoint {

    double lat, lon;

    public LegacyPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
