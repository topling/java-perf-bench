# java-perf-bench
Java performance benchmark collection

## ByteArrayBenchmark
```
$ bash run.sh 256 1000000 10

Start  warmup JVM: iteration = 1000000
Finish warmup JVM: elapsed = 372.380421 ms
Configurations: ArraySize = 0.25 KB, Iterations = 1000000
Java    new byte[256]: 48.18 ms, avg 48.18 ns/op
Uninit  new byte[256]: 27.20 ms, avg 27.20 ns/op
JNI NewByteArray(256): 93.96 ms, avg 93.96 ns/op
JNI  Do  Nothing 256 : 14.46 ms, avg 14.46 ns/op
Latency: java = 100%, uninit = 56.46%, jni = 195.03%
```

## GetByteArrayRegion
```
=== Performance Comparison: GetByteArrayRegion vs Java Array Copy ===
Array size: 16 bytes
Warmup iterations: 200000
Test iterations: 1000000

Starting warmup...
Warmup completed. Starting tests...

Testing JNI GetByteArrayRegion...
Testing Java System.arraycopy...
=== Performance Results ===
JNI GetByteArrayRegion:
  Total time: 17190984 ns
  Average time per call: 17.19 ns
  Throughput: 58170027 calls/second

Java System.arraycopy:
  Total time: 9444879 ns
  Average time per call: 9.44 ns
  Throughput: 105877481 calls/second

=== Performance Comparison ===
Java System.arraycopy is 1.82x faster than JNI GetByteArrayRegion
```

## simple
```
$ java IntegerCreationBenchmark.java

Warmup JIT
Sleep 2000ms
Start benchmark, iteration = 1000000
Average per Integer.valueOf(1234) time: 18.733856 ns
```