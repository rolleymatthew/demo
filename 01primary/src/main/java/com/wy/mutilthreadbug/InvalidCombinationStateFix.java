package com.wy.mutilthreadbug;

import java.util.Random;

/**
 * Created by yunwang on 2021/6/30 16:01
 */
public class InvalidCombinationStateFix {
    public static void main(String[] args) {
        CombinationStatTask task = new CombinationStatTask();
        Thread thread = new Thread(task);
        thread.start();

        Random random = new Random();
        while (true) {
            int rand = random.nextInt(1000);
            JEntry jEntry =new JEntry();
            jEntry.setState(rand);
            jEntry.setDubble(rand * 2);
            task.setjEntry(jEntry);
        }
    }

    private static class CombinationStatTask implements Runnable {
        JEntry jEntry;

        public JEntry getjEntry() {
            return jEntry;
        }

        public void setjEntry(JEntry jEntry) {
            this.jEntry = jEntry;
        }

        @Override
        public void run() {
            int c = 0;
            for (long i = 0; ; i++) {
                JEntry jEntry=getjEntry();
                if (jEntry==null){
                    continue;
                }
                int i1 = jEntry.getState();
                int i2 = jEntry.getDubble();
                if (i1 * 2 != i2) {
                    c++;
                    System.err.printf("Fuck! Got invalid CombinationStat!! check time=%s, happen time=%s(%s%%), count value=%s|%s\n",
                            i + 1, c, (float) c / (i + 1) * 100, i1, i2);
                } else {
                    // 如果去掉这个输出，则在我的开发机上，发生无效组合的概率由 ~5% 降到 ~0.1%
                    System.out.printf("Emm... %s|%s\n", i1, i2);
                }
            }
        }
    }
}
