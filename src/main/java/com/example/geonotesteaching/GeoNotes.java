package com.example.geonotesteaching;

import java.time.Instant;
import java.util.List;
import java.util.Scanner;

/*
 * GeoNotes — Clase principal con una CLI sencilla.
 *
 * COSAS A FIJARSE (Java 11 → 21):
 * - Java 11: API estándar consolidada; aquí usamos Instant (java.time) para fechas.
 * - Java 14: "switch expressions" con flechas (->) y posibilidad de yield en bloques (en este archivo usamos la forma simple; ver Describe para más).
 * - Java 15: "Text Blocks" (""" ... """) — se usan en Timeline.Render para generar JSON multilínea.
 * - Java 16: "records" (GeoPoint, Note, etc.) — clases inmutables concisas con constructor canónico, equals/hashCode/toString.
 * - Java 17: "sealed classes/interfaces" (Attachment) — jerarquías cerradas que el compilador puede verificar exhaustivamente.
 * - Java 17: pattern matching para instanceof (lo verás en Describe).
 * - Java 21: "record patterns" (lo verás en Match donde se desestructura un record directamente en un switch/if).
 * - Java 21: "Sequenced Collections" (Timeline podría usar SequencedMap/LinkedHashMap.reversed(); aquí mostramos el enfoque clásico, pero coméntalo en clase).
 * - Java 21: "Virtual Threads" (demo aparte en el proyecto moderno; no se usan aquí).
 */
public class GeoNotes {

    /*
     * timeline es el "modelo" de la aplicación: guarda las notas en memoria.
     * Timeline tiene una inner class no estática (Render) que sabe exportar el contenido a JSON con Text Blocks.
     * -> OJO: inner class no estática = necesita una instancia externa para crearse (ver exportNotesToJson()).
     */
    private static final Timeline timeline = new Timeline();

    /*
     * Scanner para leer del stdin. Mantener uno único y reutilizarlo es buena práctica para la CLI.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /*
     * Generador simple de IDs. En un proyecto real, probablemente usarías UUID o una secuencia persistente.
     */
    private static long noteCounter = 1;

    // La clase principal del programa. Contiene el menú interactivo para la CLI.
    public static void main(String[] args) {
        /*
         * Modo "examples":
         * Gradle define una tarea 'examples' que invoca main con el argumento "examples".
         * Útil para mostrar rápidamente la salida JSON sin teclear en la CLI.
         */
        if (args != null && args.length > 0 && "examples".equalsIgnoreCase(args[0])) {
            seedExamples();
            exportNotesToJson();
            return;
        }
        System.out.println("--------------------------------------");
        System.out.println("  📝 Bienvenid@ a la aplicación GeoNotes");
        System.out.println("--------------------------------------");
        boolean running = true;
        while (running) {
            printMenu();
            try {

                /*
                 * Leemos la opción como String y la convertimos a int.
                 * En lugar de nextInt(), usamos nextLine()+parseInt() para evitar "pegarse" con saltos de línea restantes.
                 */
                int choice = Integer.parseInt(scanner.nextLine().trim());

                /*
                 * SWITCH EXPRESSION (Java 14):
                 * - Sintaxis con flechas (->), no hace falta 'break' y es más clara.
                 * - Si usáramos bloques complejos, podríamos usar 'yield' para devolver un valor.
                 * Aquí lo empleamos en su forma de "switch moderno" sobre efectos (no devuelve valor).
                 */
                switch (choice) {
                    case 1 -> createNote();
                    case 2 -> listNotes();
                    case 3 -> filterNotes();
                    case 4 -> exportNotesToJson();
                    case 5 -> exportMarkdown();
                    case 6 -> latest();
                    case 7 -> search();
                    case 8 -> consultLocation();
                    case 9 -> listNotesReversed();
                    case 10 -> running = false;
                    default -> System.out.println("❌ Opción no válida. Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                /*
                 * Manejo de errores "clásico" (en Kotlin tendrías null-safety y Result más idiomáticos).
                 * Aquí mostramos un mensaje claro al usuario.
                 */
                System.out.println("❌ Entrada no válida. Por favor, ingresa un número.");
            }
        }
        System.out.println("¡Gracias por usar GeoNotes! 👋");
    }

    private static void listNotesReversed() {

        System.out.println("---------------------------------");
        System.out.println("     LISTAR NOTAS (reversed)     ");
        System.out.println("---------------------------------");

        if (timeline.getNotes().isEmpty()) {
            System.out.println("No hay notas que mostrar.");
            return;
        }

        timeline.reversed().forEach(note ->
                System.out.printf("ID: %d | Titulo: %s | Contenido: %s%n",
                        note.id(), note.title(), note.content())
        );
    }

    private static void consultLocation() {

        System.out.println("---------------------------------");
        System.out.println("     CONSULTAR LA UBICACIÓN      ");
        System.out.println("---------------------------------\n");

        boolean running = true;

        while (running) {
            try {
                System.out.println("[+] Introduce la lat: ");
                double lat = Double.parseDouble(scanner.nextLine().trim());

                System.out.println("[+] Introduce la lon: ");
                double lon = Double.parseDouble(scanner.nextLine().trim());

                GeoPoint point = new GeoPoint(lat, lon);
                String whereResult = Match.where(point);

                System.out.println("[+] La localización (" + lat + "," + lon + ") es: " + whereResult);

                System.out.println("Pulsa ENTER para continuar...");
                scanner.nextLine();
                running = false;

            } catch (NumberFormatException e) {
                System.out.println("[-] ERROR: Tienes que ingresar un valor número para la latitud y la longitud!");
            } catch (Exception e) {
                System.out.println("[-] ERROR: " + e.getMessage());
            }
        }
    }

    private static void latest(){

        int n;
        boolean running = true;

        while (running) {
            try {

                System.out.println("Introduce el número de notas que desea listar:");
                n = Integer.parseInt(scanner.nextLine().trim());

                if (n <= timeline.getNotes().size()) {
                    timeline.latest(n).forEach(System.out::println);
                    running = false;
                } else {
                    throw new IllegalArgumentException("El número de notas que desea listar no es válido.");
                }

            } catch (NumberFormatException e) {
                System.out.println("[-] ERROR: Tienes que introducir un número");
            } catch (Exception e) {
                System.out.println("[-] ERROR: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n---------------------------------");
        System.out.println("               MENU              ");
        System.out.println("---------------------------------");
        System.out.println("[1] Crear una nueva nota");
        System.out.println("[2] Listar todas las notas");
        System.out.println("[3] Filtrar notas por palabra clave");
        System.out.println("[4] Exportar notas a JSON (Text Blocks)");
        System.out.println("[5] Exportar Markdown");
        System.out.println("[6] Listar últimas N");
        System.out.println("[7] Buscar avanzada");
        System.out.println("[8] Consultar ubicacion");
        System.out.println("[9] Listar notes reversed");
        System.out.println("[10] Salir");
        System.out.print("\n[+] Elige una opción: ");
    }

    private static void exportMarkdown() {
        MarkdownExporter exporter = new MarkdownExporter(timeline);
        String markdown = exporter.export();
        System.out.println("\n--- Exportando notas a Markdown ---");
        System.out.println(markdown);
    }

    private static void search(){
        String search;
        String input;

        try {

            System.out.println("[+] Buscar por rango o palabra: ");
            search = scanner.nextLine().trim();

            if (search.equalsIgnoreCase("rango")) {

                System.out.println("Introduce lat superior (topLeft):");
                double topLat = Double.parseDouble(scanner.nextLine());

                System.out.println("Introduce lon izquierda (topLeft):");
                double leftLon = Double.parseDouble(scanner.nextLine());

                System.out.println("Introduce lat inferior (bottomRight):");
                double bottomLat = Double.parseDouble(scanner.nextLine());

                System.out.println("Introduce lon derecha (bottomRight):");
                double rightLon = Double.parseDouble(scanner.nextLine());

                GeoArea area = new GeoArea(new GeoPoint(topLat, leftLon), new GeoPoint(bottomLat, rightLon));

                List<String> results = timeline.searchByArea(area);

                for (String noteStr : results) {
                    System.out.println(noteStr);
                }

            } else if (search.equalsIgnoreCase("palabra")){

                System.out.println("Introduce el titulo o contenido: ");
                input = scanner.nextLine().trim();
                timeline.searchName(input).forEach(System.out::println);

            } else {
                throw new IllegalArgumentException("No has introducido una opción correcta");
            }

        } catch (Exception e) {
            System.out.println("[-] ERROR: " + e.getMessage());
        }


    }

    private static void createNote() {
        System.out.println("\n--- Crear una nueva nota ---");

        // 'var' (Java 10) para inferencia local: útil para código más legible; en APIs públicas, mejor tipos explícitos.
        System.out.print("Título: ");
        var title = scanner.nextLine();
        System.out.print("Contenido: ");
        var content = scanner.nextLine();

        /*
         * Lectura robusta de números: mejor parsear desde nextLine() para controlar errores y limpieza del buffer.
         * (Si fuese una app real, haríamos bucles hasta entrada válida).
         */
        System.out.print("Latitud: ");
        var lat = Double.parseDouble(scanner.nextLine());
        System.out.print("Longitud: ");
        var lon = Double.parseDouble(scanner.nextLine());
        try {

            /*
             * RECORDS (Java 16):
             * - GeoPoint es un record con "compact constructor" que valida rangos (ver clase GeoPoint).
             * - Note también es record; su constructor valida title/location/createdAt.
             * Ventaja: menos boilerplate (constructor/getters/equals/hashCode/toString generados).
             */

            var geoPoint = new GeoPoint(lat, lon);

            /*
             * Instant.now() (java.time) para timestamps — la API java.time es la recomendada desde Java 8.
             * attachment lo dejamos a null en este flujo simple; podrías pedirlo al usuario.
             */
            var note = new Note(noteCounter++, title, content, geoPoint, Instant.now(), null);
            timeline.addNote(note);
            System.out.println("✅ Nota creada con éxito.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void listNotes() {
        System.out.println("\n--- Notas disponibles ---");
        if (timeline.getNotes().isEmpty()) {
            System.out.println("No hay notas creadas.");
            return;
        }

        /*
         * Bucle forEach sobre el Map<Long, Note>.
         * En Kotlin harías algo similar con forEach y String templates.
         */
        timeline.getNotes().forEach((id, note) -> System.out.printf("ID: %d | Título: %s | Contenido: %s%n", id, note.title(), note.content()));
    }

    private static void filterNotes() {
        System.out.print("\nIntroduce la palabra clave para filtrar: ");
        var keyword = scanner.nextLine();
        System.out.println("\n--- Resultados de búsqueda ---");

        /*
         * Streams (desde Java 8) — muy similares a las funciones de colección en Kotlin.
         * Filtramos por título o contenido y recogemos en una List inmutable (toList() desde Java 16 retorna una lista no modificable).
         */
        var filtered = timeline.getNotes().values().stream().filter(n -> n.title().contains(keyword) || n.content().contains(keyword)).toList();
        if (filtered.isEmpty()) {
            System.out.println("No se encontraron notas con: " + keyword);
            return;
        }
        filtered.forEach(n -> System.out.printf("ID: %d | %s | %s%n", n.id(), n.title(), n.content()));
    }

    private static void exportNotesToJson() {
        /*
         * INNER CLASS NO ESTÁTICA:
         * - Timeline.Render es una clase interna "no estática" (inner class).
         * - Por eso se instancia con: timeline.new Render()
         * - Así Render queda LIGADA a ESTA instancia de Timeline (y accede a sus 'notes').
         *
         * Si Render fuera 'static', se instanciaría como 'new Timeline.Render(timeline)' pasando la Timeline explícita.
         */
        var renderer = timeline.new Render(); // ¿Por qué esto no funciona new Timeline().new Render();?

        /*
         * TEXT BLOCKS (Java 15) — ver Timeline.Render:
         * - Allí se usan literales de cadena multilínea """ ... """ para construir JSON legible.
         * - Se normaliza la indentación y no necesitas escapar comillas constantemente.
         */
        String json = renderer.export();

        System.out.println("\n--- Exportando notas a JSON ---");
        System.out.println(json);
    }

    private static void seedExamples() {
        /*
         * Semilla de ejemplo para la tarea Gradle 'examples'.
         * También aquí vemos la jerarquía sellada (sealed) Attachment con tres records:
         *   Photo, Audio, Link — y cómo se pasan a Note como polimorfismo clásico.
         */
        timeline.addNote(new Note(noteCounter++, "Cádiz", "Playita", new GeoPoint(36.5297, -6.2927), Instant.now(), new Photo("u", 2000, 1000)));
        timeline.addNote(new Note(noteCounter++, "Sevilla", "Triana", new GeoPoint(37.3826, -5.9963), Instant.now(), new Audio("a", 320)));
        timeline.addNote(new Note(noteCounter++, "Córdoba", "Mezquita", new GeoPoint(37.8790, -4.7794), Instant.now(), new Link("http://cordoba", "Oficial")));
        /*
         * DONDE VER EL RESTO DE NOVEDADES:
         * - Pattern matching para instanceof + switch con guardas 'when': ver Describe.
         * - Record patterns (Java 21): ver Match (desestructurar GeoPoint en switch/if).
         * - SequencedMap / reversed(): ver Timeline (versión moderna). En este “teaching” usamos LinkedHashMap clásico,
         *   pero explica a los alumnos que en Java 21 LinkedHashMap implementa SequencedMap y se puede pedir la vista invertida.
         * - Virtual Threads: demo aparte en el otro proyecto “moderno” (no se usan aquí).
         */
    }
}