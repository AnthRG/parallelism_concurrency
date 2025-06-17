package edu.pucmm;

import java.util.concurrent.atomic.AtomicBoolean;

public class MatrixSearchThread extends Thread {
    private final int[][] datos;
    private final int inicio, fin;
    AtomicBoolean encontrado;
    private int x;
    private int y;
    private int numero;
    private boolean encontradoFlag;

    public MatrixSearchThread(int[][] datos, int inicio, int fin, int numero, AtomicBoolean encontrado) {
        this.datos = datos;
        this.inicio = inicio;
        this.fin = fin;
        this.numero = numero;
        x = 0;
        y = 0;
        this.encontrado = encontrado;
        encontradoFlag = false;
    }

    @Override
    public void run() {
        for(int i = inicio; i < fin && !encontrado.get(); i++) {
            for (int j = 0; j < datos.length && !encontrado.get(); j++) {
                if (datos[i][j] == numero) {
                    x = i;
                    y = j;
                    encontrado.set(true);
                    encontradoFlag = true;
                }
            }
        }
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public AtomicBoolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(Boolean encontrado) {
        this.encontrado.set(encontrado);
    }
    public boolean isEncontradoFlag() {
        return encontradoFlag;
    }
}
