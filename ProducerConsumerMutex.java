// File: ProducerConsumerMutex.java
import java.util.concurrent.locks.*;
import java.util.*;

public class ProducerConsumerMutex {
    static class Buffer {
        private final Queue<Integer> q = new LinkedList<>();
        private final int capacity;
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        Buffer(int c){capacity=c;}
        public void produce(int val) throws InterruptedException {
            lock.lock();
            try{
                while(q.size()==capacity) notFull.await();
                q.add(val);
                System.out.println("Produced: "+val);
                notEmpty.signal();
            } finally { lock.unlock(); }
        }
        public int consume() throws InterruptedException {
            lock.lock();
            try{
                while(q.isEmpty()) notEmpty.await();
                int v = q.remove();
                System.out.println("Consumed: "+v);
                notFull.signal();
                return v;
            } finally { lock.unlock(); }
        }
    }
    public static void main(String[] args) {
        Buffer buf = new Buffer(5);
        Thread producer = new Thread(() -> {
            for(int i=1;i<=10;i++){
                try{ buf.produce(i); Thread.sleep(200); } catch(Exception e){ e.printStackTrace(); }
            }
        });
        Thread consumer = new Thread(() -> {
            for(int i=1;i<=10;i++){
                try{ buf.consume(); Thread.sleep(400); } catch(Exception e){ e.printStackTrace(); }
            }
        });
        producer.start(); consumer.start();
        try{ producer.join(); consumer.join(); } catch(InterruptedException e){ }
    }
}
