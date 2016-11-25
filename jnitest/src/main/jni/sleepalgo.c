//
// Created by Xie on 2016/9/21.
//

#include "sleepalgo.h"
#include "jni.h"
#include <stddef.h>
#include "eegsmart_jnitest_JNIUtils.h"

JNIEXPORT jstring JNICALL Java_eegsmart_jnitest_JNIUtils_sleepalgo(JNIEnv *env, jclass clazz, jint arg, jcharArray arr){
        int len=(*env)->GetArrayLength(env,arr);
        jchar *array;
        jboolean iscopy;
        array = (*env)->GetCharArrayElements(env,arr,NULL);

        return (*env)->NewStringUTF(env, "hello world jni:" + 2);

}