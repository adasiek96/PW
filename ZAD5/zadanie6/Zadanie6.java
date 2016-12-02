package zadanie6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie6 {
    
    private static final int N_WIERSZY = 10;
    private static final int N_KOLUMN = 100;
    private static final int N_WĄTKÓW = 4;
    private static List<Future<Integer>>[] dane = new List[N_WIERSZY];
    
    private static class Oblicz implements Callable<Integer> {
        private final int x;

        private Oblicz(final int i, final int k) {
            this.x = Macierz.wartość(i, k);
        }

        @Override
        public Integer call() {
            return x;
        }
    }
    
    public static void main(final String[] args) {
        System.out.println("Zadanie 6");
        // deklaracja puli wątków
        final ExecutorService pulaWątków =
                Executors.newFixedThreadPool(N_WĄTKÓW);
        // inicjalizacja tablicy list z danymi
        for (int i = 0; i < N_WIERSZY; i++) {
            final List<Future<Integer>> lista = new ArrayList<>();
            dane[i] = lista;
        }
        // rozpoczęcie pracy przez pulę wątków
        for (int k = 0; k < N_KOLUMN; k++) {
            for (int i = 0; i < N_WIERSZY; i++) {
                dane[i].add(pulaWątków.submit(new Oblicz(i, k)));
                }
        }
        // sumowanie wierszy
        for (int i = 0; i < N_WIERSZY; i++) {
            int suma = 0;
            for (int k = 0; k < N_KOLUMN; k++) {
                try {
                    suma += dane[i].get(k).get();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(Zadanie6.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
            System.out.println(i + " " + suma);
        }
        pulaWątków.shutdown();
    }
}