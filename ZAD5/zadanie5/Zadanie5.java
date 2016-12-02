package zadanie5;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie5 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final ConcurrentMap<Integer, BlockingQueue<Integer>> dane3 =
            new ConcurrentHashMap<>();
    public static void main(final String[] args) {
        System.out.println("Zadanie 5");
        for (int k = 0; k < N_KOLUMN; ++k) {
            new Thread(new Wątek(k)).start();
        }
        for (int i = 0; i < N_WIERSZY; i++) {
            int suma = 0;
            for (int k = 0; k < N_KOLUMN; k++) {
                try {
                    suma += dane3.get(i).take();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Zadanie5.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
            dane3.get(i).clear();
            System.out.println(i + " " + suma);
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
                try {
                    dane3.putIfAbsent(i, new LinkedBlockingQueue<Integer>());
                    dane3.get(i).put(Macierz.wartość(i, któraKolumna));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Zadanie5.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
    }
}
