package com.wy.mutilthreadbug;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yunwang on 2021/6/30 16:22
 */
public class WrongCounterFix {
    private static final int INC_COUNT = 100000000;

    volatile AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        WrongCounterFix demo = new WrongCounterFix();

        System.out.println("Start task thread!");
        Thread thread1 = new Thread(demo.getConcurrencyCheckTask());
        thread1.start();
        Thread thread2 = new Thread(demo.getConcurrencyCheckTask());
        thread2.start();

        thread1.join();
        thread2.join();

        AtomicInteger actualCounter = demo.counter;
        int expectedCount = INC_COUNT * 2;
        if (actualCounter.get() != expectedCount) {
            // 在我的开发机上，几乎必现！即使counter上加了volatile。（简单安全的解法：使用AtomicInteger）
            System.err.printf("Fuck! Got wrong count!! actual %s, expected: %s.", actualCounter, expectedCount);
        } else {
            System.out.println("Wow... Got right count!");
        }
    }

    ConcurrencyCheckTask getConcurrencyCheckTask() {
        return new ConcurrencyCheckTask();
    }

    private class ConcurrencyCheckTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < INC_COUNT; ++i) {
                counter.incrementAndGet();
            }
        }
    }

}
