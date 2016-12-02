package zadanie3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie3 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static List<Integer>[] dane = new List[N_WIERSZY];
    private static final CountDownLatch[] zatrzask =
            new CountDownLatch[N_WIERSZY];
    private static volatile int wiersz = 0;
    
    public static void main(final String[] args) {
        System.out.println("Zadanie 3");
        for (int i = 0; i < N_WIERSZY; i++) {
            zatrzask[i] = new CountDownLatch(N_KOLUMN);
            List<Integer> lista = Collections.synchronizedList(new ArrayList<>());
            dane[i] = lista;
        }
        for (int k = 0; k < N_KOLUMN; ++k) {
            new Thread(new Wątek(k)).start();
        }
        for (int i = 0; i < N_WIERSZY; i++) {
            try {
                zatrzask[i].await();
                //System.out.println("otwarto " + i);
                int suma = 0;
                //System.out.println("size = " + dane[i].size());
                for (int k = 0; k < N_KOLUMN; ++k) {
                    suma += dane[i].remove(0);
                }
                System.out.println(i + " " + suma);
            } catch (InterruptedException ex) {
                Logger.getLogger(Zadanie3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static class Wątek implements Runnable {

        private final int któraKolumna;

        private Wątek(final int któraKolumna) {
            this.któraKolumna = któraKolumna;
        }

        @Override
        public void run() {
            for (int i = 0; i < N_WIERSZY; i++) {
                //if (i == 0) System.out.println(Thread.currentThread().getName());
                dane[i].add(Macierz.wartość(i, któraKolumna));
                //System.out.println(dane[któraKolumna]);
                //System.out.println(i + " " + któraKolumna);
                zatrzask[i].countDown();
            }
        }
    }
}
