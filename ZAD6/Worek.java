package zadania;

import java.util.HashMap;
import java.util.Map;

public class Worek<T> {
    
    // Hashmapa 'mapa', gdzie: kluczem jest argument, a wartością - ilość
    // elementów w hashmapie równych argumentowi z klucza
    Map<T, Integer> mapa = new HashMap<>();
    
    // Hashmapa 'grupa', gdzie: kluczem jest argument, a wartością - obiekt
    // na którym czekają wątki, chcące wyjąć element równy argumentowi z klucza
    Map<T, Object> grupa = new HashMap<>();
    
    // 'mutex' obiekt, służący ochronie zmiennych, na którym czekają wszystkie
    // wątki
    final Object mutex = new Object();
    
    public void włóż(T argument) {
        synchronized (mutex) {
            if (mapa.containsKey(argument)) {
                mapa.put(argument, mapa.get(argument) + 1);
            }
            else {
                mapa.put(argument, 1);
                grupa.put(argument, new Object());
            }
        }
        synchronized (grupa.get(argument)) {
            grupa.get(argument).notifyAll();
        }
    }
    
    public void wyjmij(T argument) throws InterruptedException {
        synchronized (mutex) {
            if (!mapa.containsKey(argument)) {
                mapa.put(argument, 0);
                grupa.put(argument, new Object());
            }
        }
        synchronized (grupa.get(argument)) {
            while (true) {
                if (mapa.get(argument) > 0) {
                    break;
                }
                grupa.get(argument).wait();
            }
            synchronized (mutex) {
                mapa.put(argument, mapa.get(argument) - 1);
            }
        }
    }
}
