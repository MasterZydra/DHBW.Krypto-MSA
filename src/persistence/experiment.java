package persistence;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

class experiment {
    public static void main(String[] args) {

        StringBuilder a = new StringBuilder();
        Stopwatch timer = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            a.append(Integer.toBinaryString(i));
            if (i%100==0)System.out.println(timer.elapsed(TimeUnit.MILLISECONDS));
        }
        System.out.println("Method took: " + timer.stop());
        System.out.println(a.toString().length());
    }
}