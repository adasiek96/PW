package zadanie1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Zadanie1 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final int[] dane = new int[N_KOLUMN];
    private static final CyclicBarrier bariera;
    
    static {
        bariera = new CyclicBarrier(N_KOLUMN, new Runnable() {
            @Override
            public void run() {
                //System.out.println("Policzono wiersz");
                int suma = 0;
                for (int k = 0; k < N_KOLUMN; ++k) {
                    suma += dane[k];
                }
                System.out.println(" " + suma);
            }
        });
    }
    
    public static void main(final String[] args) {
        System.out.println("Zadanie 1");
        //System.out.println("Sumy kolejnych wierszy: ");
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
                    dane[któraKolumna] =
                            Macierz.wartość(i, któraKolumna);
                    //System.out.println(dane[któraKolumna]);
                    //System.out.println(i + " " + któraKolumna);
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
