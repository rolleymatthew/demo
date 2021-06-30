package com.wy.mutilthreadbug;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yunwang on 2021/6/30 15:33
 * https://coolshell.cn/articles/9606.html
 * ConcurrentHashMap支持多线程
 */
public class HashMapHangDemoFix {
    final Map<Integer, Object> holder = new ConcurrentHashMap<Integer, Object>();

    public static void main(String[] args) {
        HashMapHangDemoFix demo = new HashMapHangDemoFix();
        for (int i = 0; i < 100; i++) {
            demo.holder.put(i, i);
        }

        Thread thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();
        thread = new Thread(demo.getConcurrencyCheckTask());
        thread.start();

        System.out.println("Start get in main!");
        for (int i = 0; ; ++i) {
            for (int j = 0; j < 10000; ++j) {
                demo.holder.get(j);

                // 如果出现hashmap的get hang住问题，则下面的输出就不会再出现了。
                // 在我的开发机上，很容易在第一轮就观察到这个问题。
                System.out.printf("Got key %s in round %s\n", j, i);
            }
        }
    }

    HashMapHangDemoFix.ConcurrencyTask getConcurrencyCheckTask() {
        return new HashMapHangDemoFix.ConcurrencyTask();
    }

    private class ConcurrencyTask implements Runnable {
        Random random = new Random();

        @Override
        public void run() {
            System.out.println("Add loop started in task!");
            while (true) {
                holder.put(random.nextInt() % (1024 * 1024 * 100), null);
            }
        }
    }

}
