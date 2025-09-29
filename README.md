# GeoNotesTeaching (Java 21)

Proyecto docente con Java clásico + moderno (records, sealed, text blocks, switch expression, pattern matching).
Incluye Gradle Wrapper (scripts) para facilitar la ejecución.

## Ejecutar
- IntelliJ: Abrir carpeta y ejecutar tarea Gradle `run` o `examples`.
- Terminal:
  ```bash
  ./gradlew run
  ./gradlew examples
  ```
  
## Notas sobre decisiones de diseño y Java vs Kotlin

1. **Sealed Interface para Attachments**
   - Hemos usado una sealed interface Attachment permits Photo, Audio, Link, Video porque es asi el compilador, y me obliga a manejar todos los casos en los switch.
   - Si añado un nuevo tipo de attachment, el codigo no compila hasta que lo añada en todos lados => entonces hay menos bugs.
   - Java VS Kotlin: Kostlin tiene una sealed class que funciona igual pero con menos codigo.
   
2. **Records en vez de clases normales**
   - Los records en java 16+ me ahorran escribir los getters, equals, hashCode, y toString, debido a que java te lo hace automaticamente.
   - Tambien son inmutables por defecto, lo cual esta muy bien para evitar cambios accidentales.
   - Java VS Kotlin: Kotlin tiene una data class que hace lo mismo pero puedes usar var si necesitas mutabilidad
3. **SequencedMap (Java 21)**
   - Usamos el SequencedMap porque ofrece buenas practicas al mantener el orden de insercion y tiene el metodo .reserved().
   - Es mas moderno que usar LinkedHashMap normal.
   - Java VS Kotlin: en kotlin no tiene SequencedMap especifico pero con extension functions se podria hacer lor mismo realmente.
4. **Pattern Matching en Switch**
   - El switch moderno de java 21 con el mattern matching hace el codigo mas limpio
   - En vez de poner un monton de if (x instanceof Photo), simplemente lo que hago es poner un case Photo p -> y ya está.
   - Java VS Kotlin: el when de kotlin hace esto desde siempre y con los smart case automaticos no haria falta declarar el p.

## Notas adicionales sobre Java vs Kotlin

**Java**
- En Java hay que checkear el null manualmente en todos los lados, y es mas verboso con .formatted() y escribir tipos mas explicitos
- Aun asi lo bueno de Java 21 son los text blocks para JSON multilinea, y los switch expressions con guardados when.

**Kotlin**
- Si fuera en Kotlin no habria que hacer el ejercicio F2 porque el null safety se hace automaticamente
- Habria menos lineas de codigo en general, porque es menos verboso
- Se haria String templates en vez de .formatted()
- Y el when es mas potente que switch.

Java se esta intentando modernizar y tomarle el ritmo a kotlin, especialmente en java 21 que esta modernizandose, intentando imitar las cosas que Kotlin lleva haciendo desde hace años.
