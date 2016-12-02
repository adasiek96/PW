import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PW3 {
    public static void main(String[] args) {
        Zakład q = new Zakład();
        new Fryzjer(q);
        new Klient(q, 1);
        new Klient(q, 2);
        new Klient(q, 3);
        new Klient(q, 4);
        new Klient(q, 5);
        new Klient(q, 6);
        new Klient(q, 7);
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(PW3.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Klient(q, 8);
        new Klient(q, 9);
    }
}

class Zakład {
    static Semaphore semKlient = new Semaphore(0);
    static Semaphore semFryzjerŚpi = new Semaphore(0);
    static Semaphore semPoczekalnia = new Semaphore(4);
    static Semaphore semKoniecStrzyżenia = new Semaphore(0);
    void klient() {
        try {
            if (!semKlient.tryAcquire()) {
                if (semPoczekalnia.tryAcquire()) {
                    semFryzjerŚpi.release();
                    System.out.println(Thread.currentThread().getName() + " czeka w poczekalni");
                    semKlient.acquire();
                    semPoczekalnia.release();
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " wychodzi z powodu braku miejsc w poczekalni");
                    return;
                }
            }
            else {
                semFryzjerŚpi.release();
            }
            System.out.println(Thread.currentThread().getName() + " wszedł do fryzjera");
            //semPoczekalnia.release();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
        // strzyżenie - sekcja krytyczna
        System.out.println(Thread.currentThread().getName() + " jest w trakcie strzyżenia");
        
        try {
            semKoniecStrzyżenia.acquire();
            System.out.println(Thread.currentThread().getName() + " wyszedł od fryzjera");
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Zakład.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        semKlient.release();
    }
    void fryzjer() {
        semKlient.release();
        while (true) {
            try {
                if (!semFryzjerŚpi.tryAcquire()) {
                    System.out.println(Thread.currentThread().getName() + " zasypia");
                    semFryzjerŚpi.acquire();
                }
                else {
                    System.out.println(Thread.currentThread().getName() + " zawołał następnego klienta");
                }
                // strzyżenie
                Thread.currentThread().sleep(10);
                semKoniecStrzyżenia.release();
                // oczekiwanie fryzjera na wyjście poprzedniego klienta i wejście kolejnego
                while (semKoniecStrzyżenia.availablePermits() > 0) {}
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
    }
}

class Fryzjer implements Runnable {
    Zakład q;
    Fryzjer(Zakład q) {
            this.q = q;
            System.out.println("Fryzjer rozpoczyna pracę");
            new Thread(this, "Fryzjer").start();
    }
    @Override
    public void run() {
        q.fryzjer();
    }
}

class Klient implements Runnable {
    Zakład q;
    Klient(Zakład q, int i) {
        this.q = q;
        System.out.println("Klient " + i + " wchodzi do zakładu");
        new Thread(this, "Klient " + i).start();
    }
    @Override
    public void run() {
        q.klient();
    }
}

