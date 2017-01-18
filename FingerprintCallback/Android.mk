LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES += android-support-v7-appcompat \
                               android-support-v4
LOCAL_SRC_FILES := $(call all-java-files-under, src) \
                   src/com/android/internal/policy/IKeyguardExitCallback.aidl \
                   src/com/android/internal/policy/IKeyguardStateCallback.aidl \
                   src/com/android/internal/policy/IKeyguardService.aidl \
                   src/com/android/internal/policy/IKeyguardDrawnCallback.aidl \
                   src/com/android/aidl/IFingerprintManager.aidl \
                   src/com/android/aidl/IAuthenticateCallback.aidl \
                   src/com/android/aidl/IEnrollCallback.aidl

LOCAL_PROGUARD_ENABLED:= disabled  
LOCAL_PACKAGE_NAME := FingerprintCallback
LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)

