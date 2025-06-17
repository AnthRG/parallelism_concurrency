package edu.pucmm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author me@fredpena.dev
 * @created 02/06/2025  - 20:46
 */
public class ProducerConsumer {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ExecutorService executor = Executors.newFixedThreadPool(PRODUCER_COUNT + CONSUMER_COUNT);

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            executor.submit(new Producer(queue));
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            executor.submit(new Consumer(queue));
        }

        executor.shutdown();

        // Sugerencia: Usar ExecutorService o crear threads manualmente para iniciar Productores y Consumidores
    }

    static class Producer implements Runnable {
        private BlockingQueue<Integer> queue;
        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            // Generar PRODUCE_COUNT números aleatorios y colocarlos en la cola
            // Sugerencia: usar Thread.sleep(10) para simular tiempo de producción
            for (int i = 0; i < PRODUCE_COUNT; i++) {
                try {
                    int number = (int) (Math.random() * 100);
                    System.out.println("Produciendo: " + number);
                    queue.put(number);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Productor interrumpido: " + e.getMessage());
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            // Extraer elementos de la cola y procesarlos (ej: sumarlos)
            // Sugerencia: llevar la suma total por hilo y reportar al final
            int totalSum = 0;
            while (true) {
                try {
                    Integer number = queue.take();
                    if (number == null) break;
                    totalSum += number;
                    System.out.println("Consumido: " + number + ", Suma parcial: " + totalSum);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Consumidor interrumpido: " + e.getMessage());
                    break;
                }
            }
        }
    }
}