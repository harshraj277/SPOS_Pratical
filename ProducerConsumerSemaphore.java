// File: ProducerConsumerSemaphore.java
import java.util.concurrent.*;
import java.util.*;

public class ProducerConsumerSemaphore {
    static class Buffer {
        Queue<Integer> q = new LinkedList<>();
        int capacity;
        Semaphore empty, full;
        final Object lock = new Object();
        Buffer(int c){capacity=c; empty = new Semaphore(c); full = new Semaphore(0);}
        void produce(int v) throws InterruptedException {
            empty.acquire();
            synchronized(lock){
                q.add(v);
                System.out.println("Produced: "+v);
            }
            full.release();
        }
        int consume() throws InterruptedException {
            full.acquire();
            int val;
            synchronized(lock){
                val = q.remove();
                System.out.println("Consumed: "+val);
            }
            empty.release();
            return val;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Buffer buf = new Buffer(5);
        Thread p = new Thread(() -> { for(int i=1;i<=10;i++){ try{ buf.produce(i); Thread.sleep(150);}catch(Exception e){}}});
        Thread c = new Thread(() -> { for(int i=1;i<=10;i++){ try{ buf.consume(); Thread.sleep(300);}catch(Exception e){}}});
        p.start(); c.start(); p.join(); c.join();
    }
}
