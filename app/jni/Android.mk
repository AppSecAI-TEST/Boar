LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
OPENCV_LIB_TYPE:=STATIC
OPENCV_INSTALL_MODULES:=on
OPENCV_CAMERA_MODULES:=on
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")


include F:\opencv\OpenCV-300android-sdk\sdk\native\jni\OpenCV.mk

else
include $(OPENCV_MK_PATH)
endif

LOCAL_MODULE := mainSperm
LOCAL_SRC_FILES := mainSperm.cpp \
                    CountSperm.cpp
LOCAL_LDLIBS += -lm -llog
include $(BUILD_SHARED_LIBRARY)