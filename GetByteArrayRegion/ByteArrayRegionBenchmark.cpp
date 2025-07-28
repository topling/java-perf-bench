// ByteArrayRegionBenchmark.cpp
#include <jni.h>
#include <chrono>
#include "ByteArrayRegionBenchmark.h"

#ifdef __cplusplus
extern "C" {
#endif

// Warmup function
JNIEXPORT void JNICALL Java_ByteArrayRegionBenchmark_warmupGetByteArrayRegion
  (JNIEnv *env, jclass clazz, jbyteArray jarray, jint iterations) {

    jsize len = env->GetArrayLength(jarray);
    jbyte* buffer = new jbyte[len];

    for (int i = 0; i < iterations; i++) {
        env->GetByteArrayRegion(jarray, 0, len, buffer);
    }

    delete[] buffer;
}

// Performance test function
JNIEXPORT jlong JNICALL Java_ByteArrayRegionBenchmark_testGetByteArrayRegion
  (JNIEnv *env, jclass clazz, jbyteArray jarray, jint iterations) {

    jsize len = env->GetArrayLength(jarray);
    jbyte* buffer = new jbyte[len];

    auto start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < iterations; i++) {
        env->GetByteArrayRegion(jarray, 0, len, buffer);
    }

    auto end = std::chrono::high_resolution_clock::now();

    delete[] buffer;

    auto duration = std::chrono::duration_cast<std::chrono::nanoseconds>(end - start);
    return duration.count();
}

#ifdef __cplusplus
}
#endif