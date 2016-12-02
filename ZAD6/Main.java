package zadania;

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    private static final int N_WĄTKÓW = 20;
    private static final Thread wątki[] = new Thread[N_WĄTKÓW];
    private static final Worek<Integer> worek = new Worek<>();
    
    public static void main(String[] args) {
        
        Random generator = new Random();
        
        // niektóre wątki będą tylko do wkładania, a inne tylko do wyjmowania
        for (int i = 0; i < N_WĄTKÓW; ++i) {
            int r = generator.nextInt(5);
            int b = generator.nextInt(3);
            if (b != 0 && i < N_WĄTKÓW / 2) {
                wątki[i] = new Thread(new Wątek(r, false));
            }
            else {
                wątki[i] = new Thread(new Wątek(r, true));
            }
            wątki[i].start();
        }
        
        try {
            sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        int v = 0;
        for (int i = 0; i < N_WĄTKÓW; ++i) {
            if (wątki[i].isAlive()) {
                v++;
                wątki[i].interrupt();
            }
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (v != 0) {
            System.out.println("OK. Nie wszystkie wątki wyjmujące doczekały się"
                    + " włożenia oczekiwanej wartości.");
        }
        else {
            System.out.println("OK. Wszystkie wątki zostały zakończone.");
        }
    }
    
    private static class Wątek implements Runnable {

        private final int argument;
        private final boolean czyWrzuca; // true - wkłada, false - wyjmuje

        private Wątek(int argument, boolean czyWrzuca) {
            this.argument = argument;
            this.czyWrzuca = czyWrzuca;
        }

        @Override
        public void run() {
            if(czyWrzuca) {
                System.out.println(Thread.currentThread().getName()
                        + ": włożyłem " +  argument);
                worek.włóż(argument);
            }
            else {
                try {
                    System.out.println(Thread.currentThread().getName()
                            + ": chcę wyjąć " + argument);
                    worek.wyjmij(argument);
                    System.out.println(Thread.currentThread().getName()
                            + ": wyjąłem " + argument);
                } catch (InterruptedException ex) {
                    System.out.println("Wątek "
                            + Thread.currentThread().getName()
                            + " nie doczekał się włożenia oczekiwanej"
                            + " wartości.");
                }
            }
        }
    }
}
