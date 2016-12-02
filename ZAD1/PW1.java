public class PW1 {
    
    public static void main(String[] args) {
        
        Wektor w = new Wektor(5);
        
        int n = 3;
        int[] tab1 = new int[n];
        tab1[0] = 2;
        tab1[1] = 4;
        tab1[2] = 6;
        
        int[] tab2 = new int[n];
        tab2[0] = 3;
        tab2[1] = 5;
        tab2[2] = 9;
        
        Wektor a = new Wektor(n, tab1);
        Wektor b = new Wektor(n, tab2);
        
        Wektor c = a.dodaj(b);
        
        wyświetl(n, tab1, tab2);
        c.wyświetl();
        
    }
    
    private static void wyświetl(int n, int[] tab1, int[] tab2) {
        System.out.print("[ ");
        for (int i = 0; i < n; i++) {
            System.out.print(tab1[i] + " ");
        }
        System.out.print("] + ");
        System.out.print("[ ");
        for (int i = 0; i < n; i++) {
            System.out.print(tab2[i] + " ");
        }
        System.out.print("] = ");
    }
    
}

class Wektor {
    private int n;
    private int tab[];
    
    Wektor(int N) {
        this.n = N;
        this.tab = new int[n];
        for (int i = 0; i < n; i++) {
            tab[i] = 0;
        }
    }
    
    Wektor(int N, int[] skladowe) {
        this.n = N;
        this.tab = new int[n];
        System.arraycopy(skladowe, 0, tab, 0, n);
    }
    
    Wektor dodaj(Wektor w) {
        Thread[] threads = new Thread[this.n];
        Integer[] results = new Integer[this.n];
        // wykonanie wielowątkowych obliczeń
        for (int i = 0; i < this.n; i++) {
            threads[i] = new Wątek(this, w, i, this.n, results);
        }
        // oczekiwanie na zakończenie wykonywania obliczeń
        for (int i = 0; i < this.n; i++) {
            while (threads[i].isAlive())
		try {
                    Thread.sleep(100);
		} catch (InterruptedException e) {
                }
        }
        // utworzenie wektora wynikowego
        int[] skladowe = new int[this.n];
        for (int j = 0; j < this.n; j++) {
            skladowe[j] = results[j].intValue();
        }
        Wektor c = new Wektor(this.n, skladowe);
        return c;
    }
    
    int getElement(int i) {
        return this.tab[i];
    }
    
    void wyświetl() {
        System.out.print("[ ");
        for (int i = 0; i < n; i++) {
            System.out.print(tab[i] + " ");
        }
        System.out.println("]");
    }
    
}

class Wątek extends Thread {
    
    private Integer[] results;
    private Wektor a, b;
    private int i, n;
    
    Wątek(Wektor a, Wektor b, int i, int n, Integer[] results) {
        this.results = results;
        this.a = a;
        this.b = b;
        this.i = i;
        this.n = n;
        this.start();
    }
    
    @Override
    public void run() {
        this.results[i] = a.getElement(this.i) + b.getElement(this.i);
        // System.out.println(Thread.currentThread().getName());
    }
}
