//
// Created by treason on 16/8/7.
//
#include "jni.h"

/*
 * Class:     com_mjiayou_trecore_test_jni_TestTreJNIUtil
 * Method:    getHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_mjiayou_jnidemo_JNIUtil_getHello
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env,
                                "Hello, I am from testjni. And who are you? Whatever, fuck you!");
}