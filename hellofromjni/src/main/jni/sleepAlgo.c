//
// Created by Xie on 2016/9/21.
//

#include "sleepAlgo.h"
#include "jni.h"
#include <stddef.h>

#define  LOG_TAG    "native-dev"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_eegsmart_hellofromjni_Jni_sleepAlgo(JNIEnv *env, jclass clazz, jint arg, jcharArray arr){
          int len=(*env)->GetArrayLength(env,arr);
          jchar *array;
          jboolean iscopy;
          array = (*env)->GetCharArrayElements(env,arr,NULL);
          LOGE(len);
          return (*env)->NewStringUTF(env, "hello world jni:" + 2);

}