// ByteArrayRegionBenchmark.java
public class ByteArrayRegionBenchmark {
    static {
        System.loadLibrary("benchmark");
    }

    // JNI method declarations
    public static native long testGetByteArrayRegion(byte[] array, int iterations);
    public static native void warmupGetByteArrayRegion(byte[] array, int iterations);

    // Java implementation for comparison
    public static long testJavaArrayCopy(byte[] src, int iterations) {
        byte[] dest = new byte[src.length];
        long start = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            System.arraycopy(src, 0, dest, 0, src.length);
        }

        long end = System.nanoTime();
        return end - start;
    }

    public static void warmupJavaArrayCopy(byte[] src, int iterations) {
        byte[] dest = new byte[src.length];
        for (int i = 0; i < iterations; i++) {
            System.arraycopy(src, 0, dest, 0, src.length);
        }
    }

    public static void main(String[] args) {
        int arraySize = 16;
        int warmupIterations = 200000;
        int testIterations = 1000000;

        // Create test array
        byte[] testArray = new byte[arraySize];
        for (int i = 0; i < arraySize; i++) {
            testArray[i] = (byte)(i & 0xFF);
        }

        System.out.println("=== Performance Comparison: GetByteArrayRegion vs Java Array Copy ===");
        System.out.println("Array size: " + arraySize + " bytes");
        System.out.println("Warmup iterations: " + warmupIterations);
        System.out.println("Test iterations: " + testIterations);
        System.out.println();

        // Warmup phase
        System.out.println("Starting warmup...");
        warmupGetByteArrayRegion(testArray, warmupIterations);
        warmupJavaArrayCopy(testArray, warmupIterations);

        // Additional warmup time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Warmup completed. Starting tests...\n");

        // Test JNI GetByteArrayRegion
        System.out.println("Testing JNI GetByteArrayRegion...");
        long jniTime = testGetByteArrayRegion(testArray, testIterations);
        double jniAvg = (double) jniTime / testIterations;

        // Test Java System.arraycopy
        System.out.println("Testing Java System.arraycopy...");
        long javaTime = testJavaArrayCopy(testArray, testIterations);
        double javaAvg = (double) javaTime / testIterations;

        // Results
        System.out.println("=== Performance Results ===");
        System.out.println("JNI GetByteArrayRegion:");
        System.out.println("  Total time: " + jniTime + " ns");
        System.out.println("  Average time per call: " + String.format("%.2f", jniAvg) + " ns");
        System.out.println("  Throughput: " + String.format("%.0f", testIterations / (jniTime / 1_000_000_000.0)) + " calls/second");
        System.out.println();

        System.out.println("Java System.arraycopy:");
        System.out.println("  Total time: " + javaTime + " ns");
        System.out.println("  Average time per call: " + String.format("%.2f", javaAvg) + " ns");
        System.out.println("  Throughput: " + String.format("%.0f", testIterations / (javaTime / 1_000_000_000.0)) + " calls/second");
        System.out.println();

        // Comparison
        System.out.println("=== Performance Comparison ===");
        double ratio = jniAvg / javaAvg;
        if (ratio < 1.0) {
            System.out.println("JNI GetByteArrayRegion is " + String.format("%.2f", 1.0/ratio) + "x faster than Java System.arraycopy");
        } else {
            System.out.println("Java System.arraycopy is " + String.format("%.2f", ratio) + "x faster than JNI GetByteArrayRegion");
        }
    }
}