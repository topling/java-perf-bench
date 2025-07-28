public class IntegerCreationBenchmark {
    public static void main(String[] args) {
        long start, end;
        int iterations = 1000000;
        Object[] pool = new Object[256];

        System.out.println("Warmup JIT");
        for (int i = 0; i < 100000; i++) {
            pool[i&255] = Integer.valueOf(1234); // 测试对象创建
        }
        for (int i = 0; i < 100; i++) {
            System.gc();
        }
        System.out.println("Sleep 2000ms");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            // do nothing
        }
        System.out.println("Start benchmark, iteration = " + iterations);

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            pool[i&255] = Integer.valueOf(1234); // 测试对象创建
        }
        end = System.nanoTime();
        System.out.println("Average per Integer.valueOf(1234) time: "
            + ((end - start) / (double)iterations) + " ns");
    }
}