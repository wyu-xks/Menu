LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sleepalgo-jni
LOCAL_SRC_FILES := sleepalgo.c

include $(BUILD_SHARED_LIBRARY)