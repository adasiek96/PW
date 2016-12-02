package zadanie2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Zadanie2 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final List<Integer> dane =
            Collections.synchronizedList(new ArrayList<Integer>());
    private static final CyclicBarrier bariera;
    
    static {
        bariera = new CyclicBarrier(N_KOLUMN, new Runnable() {
            @Override
            public void run() {
                int suma = 0;
                for (int k = 0; k < N_KOLUMN; ++k) {
                    suma += dane.remove(0);
                }
                System.out.println(" " + suma);
            }
        });
    }
    
    public static void main(final String[] args) {
        System.out.println("Zadanie 2");
        for (int k = 0; k < N_KOLUMN; ++k) {
            new Thread(new Wątek(k)).start();
        }
    }
    
    private static class Wątek implements Runnable {

        private final int któraKolumna;

        private Wątek(final int któraKolumna) {
            this.któraKolumna = któraKolumna;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < N_WIERSZY; i++) {
                    dane.add(Macierz.wartość(i, któraKolumna));
                    if (któraKolumna == N_KOLUMN - 1) System.out.print(i);
                    bariera.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                System.err.println("Wątek " + któraKolumna + " przerwany");
            }
        }
    }
}
