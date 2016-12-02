public class PW2 {
    
    public static void main(String[] args) {
        
        int n = 6;
        Wektor w = new Wektor(n);
        
        int[] tab1 = new int[n];
        tab1[0] = 2;
        tab1[1] = 4;
        tab1[2] = 6;
        tab1[3] = 5;
        tab1[4] = 0;
        tab1[5] = 1;
        
        int[] tab2 = new int[n];
        tab2[0] = 3;
        tab2[1] = 5;
        tab2[2] = 3;
        tab2[3] = 1;
        tab2[4] = 1;
        tab2[5] = 7;
        
        Wektor a = new Wektor(n, tab1);
        Wektor b = new Wektor(n, tab2);
        
        Wektor c = a.dodaj(b);
        int v = a.razy(b);
        
        wyświetl(n, tab1, tab2, '+');
        c.wyświetl();
        
        wyświetl(n, tab1, tab2, '*');
        System.out.println(v);
        
    }
    
    private static void wyświetl(int n, int[] tab1, int[] tab2, char c) {
        System.out.print("[ ");
        for (int i = 0; i < n; i++) {
            System.out.print(tab1[i] + " ");
        }
        System.out.print("] ");
        System.out.print(c + " ");
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
        // wielowątkowe dodawanie po współrzędnych
        for (int i = 0; i < this.n; i++) {
            Runnable runner = new Wątek(this, w, i, results);
            threads[i] = new Thread(runner);
            threads[i].start();
        }
        for (int i = 0; i < this.n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
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
    
    int razy(Wektor w) {
        Thread[] threads = new Thread[this.n];
        Integer[] results = new Integer[this.n];
        // wielowątkowe mnożenie po współrzędnych
        for (int i = 0; i < this.n; i++) {
            Runnable runner = new Wątek2(this, w, i, results);
            threads[i] = new Thread(runner);
            threads[i].start();
        }
        for (int i = 0; i < this.n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        // wielowątkowe logarytmiczne dodawanie
        int j = 1, k = 0;
        while (j < this.n) {
            for (int i = 0; i + j < n; i += j * 2) {
                Runnable runner = new Wątek3(i, i + j, results);
                threads[k] = new Thread(runner);
                threads[k].start();
                k++;
            }
            j *= 2;
            for (int i = 0; i < this.n; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        // wynik znajduje się w results[0]
        return results[0];
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

class Wątek implements Runnable {
    
    private Integer[] results;
    private Wektor a, b;
    private int i;
    
    Wątek(Wektor a, Wektor b, int i, Integer[] results) {
        this.results = results;
        this.a = a;
        this.b = b;
        this.i = i;
    }
    
    @Override
    public void run() {
        this.results[i] = a.getElement(i) + b.getElement(i);
    }
}

class Wątek2 implements Runnable {
    
    private Integer[] results;
    private Wektor a, b;
    private int i;
    
    Wątek2(Wektor a, Wektor b, int i, Integer[] results) {
        this.results = results;
        this.a = a;
        this.b = b;
        this.i = i;
    }
    
    @Override
    public void run() {
        this.results[i] = a.getElement(i) * b.getElement(i);
    }
}

class Wątek3 implements Runnable {
    private Integer[] results;
    private int i, j;
    
    Wątek3(int i, int j, Integer[] results) {
        this.results = results;
        this.i = i;
        this.j = j;
    }
    
    @Override
    public void run() {
        this.results[i] = this.results[i] + this.results[j];
    }
}
