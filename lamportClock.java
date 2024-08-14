import java.util.concurrent.atomic.AtomicInteger;

public class lamportClock {
    private static AtomicInteger counter;
    private int current;

    //set initial lamport clock value
    public lamportClock(int initial) {
        counter = new AtomicInteger(initial);
        current = initial;
    }

    //get next lamport clock value
    public int getNext() {
        current = lamportClock.counter.incrementAndGet();
        return current;
    }

    //get current lamport clock value
    public int getCurrent() {
        current = lamportClock.counter.addAndGet(0);
        return current;
    }

    //compare withe other lamport clock value and return the next lamport clock value
    public int toLocallamport(int received) {
        if(received <= this.current){
            this.current = lamportClock.counter.incrementAndGet();
        }else{
            while (received > this.current) {
                if (lamportClock.counter.compareAndSet(this.current, received + 1)) {
                    this.current = received + 1;
                    break;
                } else {
                    this.current = lamportClock.counter.incrementAndGet();
                }
            }
        }
        return this.current;
    }
}
