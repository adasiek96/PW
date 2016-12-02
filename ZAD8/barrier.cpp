#include <iostream>
#include <condition_variable>
#include <thread>
#include "log.h"

using namespace std;

condition_variable cv;
mutex cv_m;

class Barrier {
public:
    Barrier (int resistance) : _resistance(resistance) {}
    void reach() {
        std::unique_lock<std::mutex> lk(cv_m);
        
        _resistance--;
        if (_resistance > 0) {
            cv.wait(lk, [this]{return _resistance <= 0;});
        }
        else if (_resistance == 0) {
            log("Budzę innych!");
            cv.notify_all();
        }
    }
private:
    int _resistance;
};

void dzialaj(int i, Barrier& b) {
    log(i, ": czekam na barierze");
    b.reach();
    log(i, ": bariera osiągnięta");
}

int main() {
    Barrier b(4);
    
    int count = 10;
    std::thread threads[count];
    for (int i = 0; i < count; i++)
        threads[i] = thread{[&b, i]{dzialaj(i, b);}};
    for (int i = 0; i < count; i++)
        threads[i].join();
    
    return 0;
}
