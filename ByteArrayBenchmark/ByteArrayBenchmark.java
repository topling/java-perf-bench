public class ByteArrayBenchmark {
    static {
        System.loadLibrary("bytearraybench");
    }
    private static final int JNI_BATCH_NUM = 0;
    private static final jdk.internal.misc.Unsafe moreUnsafe =
        jdk.internal.misc.Unsafe.getUnsafe();

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("usage: arraySize iterations jniBatchNum");
            return;
        }
        final int arraySize = Integer.parseInt(args[0]);
        final int iterations = Integer.parseInt(args[1]);
        final int jniBatchNum = Integer.parseInt(args[2]);

        System.out.println("Start  warmup JVM: iteration = " + iterations);
        long warmupStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            createUninit(arraySize);
            createInJava(arraySize);
        }
        for (int i = 0; i < iterations/Math.max(jniBatchNum,1); i++) {
            sa = createInJNI(arraySize, jniBatchNum);
        }
        long warmupEnd = System.nanoTime();
        System.out.printf("Finish warmup JVM: elapsed = %f ms%n", (warmupEnd - warmupStart) / 1e6);

        System.gc();
        System.gc();

        long javaStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            createInJava(arraySize);
        }
        long javaEnd = System.nanoTime();
        double javaTime = (javaEnd - javaStart);

        System.gc();
        System.gc();

        long uninitStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            createUninit(arraySize);
        }
        long uninitEnd = System.nanoTime();
        double uninitTime = (uninitEnd - uninitStart);

        System.gc();
        System.gc();

        long jniStart = System.nanoTime();
        for (int i = 0; i < iterations/Math.max(jniBatchNum,1); i++) {
            sa = createInJNI(arraySize, jniBatchNum);
        }
        long jniEnd = System.nanoTime();
        double jniTime = (jniEnd - jniStart);

        long jniNothingStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            sa = createInJNI(arraySize, 0);
        }
        long jniNothingEnd = System.nanoTime();
        double jniNothingTime = (jniNothingEnd - jniNothingStart);

        System.out.println("Configurations: ArraySize = " + (arraySize / 1024.0) + " KB, Iterations = " + iterations);
        System.out.printf("Java    new byte[%d]: %.2f ms, avg %.2f ns/op\n",
            arraySize, javaTime/1e6, javaTime / iterations);
        System.out.printf("Uninit  new byte[%d]: %.2f ms, avg %.2f ns/op\n",
            arraySize, uninitTime/1e6, uninitTime / iterations);
        System.out.printf("JNI NewByteArray(%d): %.2f ms, avg %.2f ns/op\n",
            arraySize, jniTime/1e6, jniTime / iterations);
        System.out.printf("JNI  Do  Nothing %d : %.2f ms, avg %.2f ns/op\n",
            arraySize, jniNothingTime/1e6, jniNothingTime / iterations);
        System.out.printf("Latency: java = 100%%, uninit = %.2f%%, jni = %.2f%%\n",
            uninitTime / javaTime * 100, jniTime / javaTime * 100);
    }

    private static void createInJava(int size) {
        byte[] array = new byte[size];
        array[0] = 1; // avoid be optimized out
        sa = array;
    }
    private static void createUninit(int size) {
        byte[] array = (byte[])moreUnsafe.allocateUninitializedArray(byte.class, size);
        array[0] = 1;
        sa = array;
    }
    static byte[] sa;

    private static native byte[] createInJNI(int size, int jniBatchNum);
}