#include <jni.h>
#include <stdlib.h>
#include "ByteArrayBenchmark.h"

extern "C" {
/*
 * Class:     ByteArrayBenchmark
 * Method:    createInJNI
 * Signature: (II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_ByteArrayBenchmark_createInJNI
(JNIEnv* env, jclass, jint size, jint jniBatchNum)
{
    jbyteArray array = nullptr;
    for (int i = 0; i < jniBatchNum; i++) {
        array = env->NewByteArray(size);
        if (array == NULL) return nullptr;

        // 防止被优化掉
        jbyte* elements = env->GetByteArrayElements(array, NULL);
        if (elements != NULL) {
            elements[0] = 1;
            env->ReleaseByteArrayElements(array, elements, 0);
        }

        // 显式删除本地引用（可选，函数返回时会自动释放）
        // env->DeleteLocalRef(array);
    }
    return array;
}
}