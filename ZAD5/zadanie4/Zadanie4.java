package zadanie4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie4 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final List<BlockingQueue<Integer>> dane2 =
            Collections.synchronizedList(
            new ArrayList<BlockingQueue<Integer>>());
    
    public static void main(final String[] args) {
        System.out.println("Zadanie 4");
        for (int i = 0; i < N_WIERSZY; i++) {
            BlockingQueue<Integer> kolejka =
                    new LinkedBlockingQueue<>();
            dane2.add(i, kolejka);
        }
        for (int k = 0; k < N_KOLUMN; ++k) {
            new Thread(new Wątek(k)).start();
        }
        for (int i = 0; i < N_WIERSZY; i++) {
            int suma = 0;
            for (int k = 0; k < N_KOLUMN; ++k) {
                try {
                    suma += dane2.get(i).take();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Zadanie4.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
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
                    dane2.get(i).put(Macierz.wartość(i, któraKolumna));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Zadanie4.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
    }
}
