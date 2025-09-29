package com.example.geonotesteaching;

import java.util.Random;
import java.util.concurrent.Executors;

/*
 *
 * G2. Demo virtual threads (muy opcional)
 * Objetivo: idea general de Loom
 *
 * - Crea VirtualDemo.runIO() que lance ~50 tareas “simuladas” (sleep 200–300 ms) con:
 * try (var exec = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) { ... }
 *
 * - Muestra el hilo actual en cada tarea. No mezclar con la lógica del proyecto (solo demo).
 *
 * */
public class VirtualDemo {

    private static final Random random = new Random();

    public static void runIO() {

        System.out.println("---------------------------------");
        System.out.println("      VIRTUAL THREADS DEMO       ");
        System.out.println("---------------------------------\n");

        int numTasks = 50;

        runVirtualThreadsDemo(numTasks);

    }

    private static void runVirtualThreadsDemo(int numTasks) {

        System.out.println("Ejecutando " + numTasks + " tasks con virtual threads...\n");

        try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 1; i <= numTasks; i++) {

                final int taskId = i;
                exec.submit(() -> {

                    try {

                        // Sleep de 200-300ms simulando I/O
                        int sleepTime = 200 + random.nextInt(100);
                        Thread.sleep(sleepTime);

                        // Mostramos el hilo actual
                        Thread thread = Thread.currentThread();
                        System.out.printf("Tarea %2d | Sleep: %3dms | Thread: %s%n",
                                taskId, sleepTime, thread.getName());

                        /*
                        *
                        * Ejemplo output:
                        * Tarea 33 | Sleep: 233ms | Thread:
                        *
                        * Como podemos observar el Thread carece de nombre, esto se debe a que el thread.getName() siempre estará vacio.
                        * El motivo de esto es que los Virtual Threads en Java 21 no tienen un nombre por defecto como tal, devolviendo una cadena vacia.
                        * La razon de porque se hace así es la necesidad de optimización en memoria.
                        * Por otra parte, los Platform Threads si que reciben nombres automáticso como "Thread-0", "Thread-1"...
                        * Mientras que Virtual Threads prioriza la eficiencia sobre el debugging
                        *
                        * */

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                });

            }

        }

        System.out.println("\n[+] Se han completado las " + numTasks + " tareas de forma satisfactoria con Virtual Threads :D");

    }

    /*
     *
     * Hago un metodo main para ejecutar la demo del virtual threads de forma independiente.
     * Para probarlo sin ejecutar toda la app de GeoNotes
     *
     * */

    public static void main(String[] args) {
        runIO();
    }

}
