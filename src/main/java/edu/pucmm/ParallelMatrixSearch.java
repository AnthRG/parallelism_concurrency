package edu.pucmm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author me@fredpena.dev
 * @created 02/06/2025  - 20:46
 */
public class ParallelMatrixSearch {

    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_COUNT = 16; // Número de hilos a usar para la búsqueda paralela
    private static final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int TARGET = 256; // Número a buscar

    public static void main(String[] args) {
        // Inicializar la matriz con valores aleatorios
        fillMatrixRandom();

        // Medir el tiempo de ejecución de la búsqueda secuencial
        long startTime = System.nanoTime();
        sequentialSearch();
        long endTime = System.nanoTime();
        System.out.println("Tiempo búsqueda secuencial: " + ((endTime - startTime) / 1_000_000) + "ms");

        // Medir el tiempo de ejecución de la búsqueda paralela
        startTime = System.nanoTime();
        parallelSearch();
        endTime = System.nanoTime();
        System.out.println("Tiempo búsqueda paralela: " + ((endTime - startTime) / 1_000_000) + "ms");
    }

    private static void sequentialSearch() {
        // Implementar búsqueda secuencial
        for(int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrix[i][j] == TARGET) {
                    System.out.println("Número encontrado en posición: (" + i + ", " + j + ")");
                    return; // Salir al encontrar el número
                }
            }
        }
    }


    private static void parallelSearch() {
        // Implementar búsqueda paralela
        // Sugerencia: usar AtomicBoolean para indicar si ya se encontró el número y detener hilos

        AtomicBoolean found = new AtomicBoolean(false);
        List<MatrixSearchThread> searchThreads = new ArrayList<>();
        int rowsPerThread = MATRIX_SIZE / THREAD_COUNT;

        for (int i = 0; i < THREAD_COUNT; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == THREAD_COUNT - 1) ? MATRIX_SIZE : startRow + rowsPerThread;
            MatrixSearchThread thread = new MatrixSearchThread(
                    matrix, startRow, endRow, TARGET, found
            );
            searchThreads.add(thread);
            thread.start();
        }

        int foundX = -1, foundY = -1;
        for (MatrixSearchThread thread : searchThreads) {
            try {
                thread.join();
                if (found.get() && thread.getX() != -1 && thread.getY() != -1 && thread.isEncontradoFlag()) {
                    foundX = thread.getX();
                    foundY = thread.getY();
                    System.out.println("Numero encontrado en: (" + foundX + ", " + foundY + ")");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!found.get())  {
            System.out.println("Number no encontrado (paralela)");
        }
    }



    private static void fillMatrixRandom() {
        Random rand = new Random();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                matrix[i][j] = rand.nextInt(1000); // Rango arbitrario
            }
        }
    }
}
