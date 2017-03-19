//
// Created by root on 17-2-15.
//
#include "com_android_jnidemo_util_JniUtil.h"
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>

//android log
#define TAG "fht"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

JavaVM *m_vm;

jfieldID fid;
jstring  jstr;
const char *str;
jobject mobj;
jobject globalobj;

jmethodID  initid;
jfieldID  resId;



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
    m_vm = vm;
    JNIEnv *env = NULL;
    jint result = -1;
    if(m_vm){
        LOGD("%s: VM init success \n",__func__);
    } else{
        LOGD("%s: VM init failed \n",__func__);
    }

    if((*vm)->GetEnv(vm,(void**)&env,JNI_VERSION_1_4) != JNI_OK){
        return result;
    }
    return  JNI_VERSION_1_4;
}

JNIEnv* getJNIEnv(int *needsDetach){
    JNIEnv* env = NULL;
    jint  result = -1;
    *needsDetach = 0;
    LOGD("%s: start \n",__func__);
    if((*m_vm)->GetEnv(m_vm,(void**) &env, JNI_VERSION_1_4) != JNI_OK){
        int status = (*m_vm)->AttachCurrentThread(m_vm,&env,0);
        if(status<0){
            LOGD("%s: failed to attach current thread \n",__func__);
            return NULL;
        }
        *needsDetach = 1;
    }
    return env;
}

JNIEXPORT void JNICALL Java_com_android_jnidemo_util_JniUtil_print
  (JNIEnv *env, jobject obj){
    LOGD("%s: =======fht print====",__func__);
  }

JNIEXPORT jstring JNICALL Java_com_android_jnidemo_util_JniUtil_getStringFromC
        (JNIEnv *env, jclass js, jstring jstr){
    char buf[1024];
    char *p;

    const jbyte *str;
    str = (*env)->GetStringUTFChars(env,jstr,NULL);//把一个Unicode字串转成UTF-8格式字串,该函数会有内存分配操作
    if(str == NULL){
        return NULL;
    }
    p = buf;
    LOGD("%s: str = %s",__func__,str);
    (*env)->ReleaseStringUTFChars(env,jstr,str);//释放GetStringUTFChars中分配的内存

    p = " i am from C !!";
    return (*env)->NewStringUTF(env,p); //newStringUTF构造java.lang.String，消耗内存
}

//jarray 是 jintarray 基类
JNIEXPORT jint JNICALL Java_com_android_jnidemo_util_JniUtil_sumArray(JNIEnv * env, jclass js, jintArray array){
    jint *buf;
    jint i,sum;
    jint length;

    length = (*env)->GetArrayLength(env,array); //获得传入数组长度

    buf = (*env)->GetIntArrayElements(env,array,NULL); //返回java数组的拷贝，buf指向数组首地址
    if(buf == NULL){
        return 0;
    }
    for(i=0;i<length;i++){
        sum += buf[i];
    }
    LOGD("%s： sum= %d",__func__,sum);
    (*env)->ReleaseIntArrayElements(env,array,buf,0);
    return sum;
}

/*
 * @return 返回一个二维数组
 * GlobalRef 当你需要在JNI层维护一个JAVA对象的引用而避免该对象被垃圾回收时，使用NewGlobalRef告诉VM不要回收对象，当本地代码最终结束对象的引用时
 * 用DeleteGlobalRef释放
 * LocalRef 每个被创建的java对象，首先会被加入一个LocalRef Table,这个Table大小有限，超出vm会报LocalRef Overflow Exception然后崩溃，通过DeleteLocalRef
 * 释放对象的LocalRef
 */

JNIEXPORT jobjectArray JNICALL Java_com_android_jnidemo_util_JniUtil_initInt2Darray
        (JNIEnv *env, jclass js, jint length){
    jobjectArray  result;
    int i;
    jclass intArrayClass = (*env)->FindClass(env,"[I");//调用FindClass获得一个一维数组"[I" 等价于java int[];
    if(intArrayClass == NULL){
        return NULL;
    }

    result = (*env)->NewObjectArray(env,length,intArrayClass,NULL); //分配一个对象数组，基本类型数组是个整体概念，它是一个对象
    if(result == NULL){
        return NULL;
    }

    for(i=0;i<length;i++){
        jint  tmp[256];
        int j;
        jintArray  intArray = (*env)->NewIntArray(env, length);
        if(intArray == NULL){
            return NULL;
        }
        for(j=0;j<length;j++){
            tmp[j] = i+j;
        }
        (*env)->SetIntArrayRegion(env,intArray,0,length,tmp);
        (*env)->SetObjectArrayElement(env,result,i,intArray);
        (*env)->DeleteLocalRef(env,intArray); //释放局部对象引用
    }
    return result;
}

void doMethod(){
    LOGD("=====start==");
    int needDetach =0;
    JNIEnv  *env = getJNIEnv(&needDetach);

    (*env)->SetIntField(env,globalobj,resId,23456);

    jstr = (*env)->NewStringUTF(env,"This is C access Field");
    if(jstr == NULL){
        return;
    }
    (*env)->SetObjectField(env, globalobj, fid, jstr);

    if(needDetach){
        (*m_vm)->DetachCurrentThread(m_vm);
    }
}

/*
 * 读取java字段值，设置字段值
 * 保证obj 和 所操作字段的对象是同一对象
 *
 */
JNIEXPORT void JNICALL Java_com_android_jnidemo_util_JniUtil_accessField
        (JNIEnv *env,jclass clas, jobject obj){


    jclass cls = (*env)->FindClass(env,"com/android/jnidemo/util/Demo");
    LOGD("%s =========in c \n",__func__);
    initid = (*env)->GetMethodID(env,cls,"<init>", "()V");

    fid = (*env)->GetFieldID(env,cls,"strDemo","Ljava/lang/String;");

    resId = (*env)->GetFieldID(env,cls,"res","I");


    globalobj = obj;
    LOGD("%s ============fid====== \n",__func__);
    if(fid == NULL){
        return;
    }

    jstr = (*env)->GetObjectField(env,obj,fid);
    str = (*env)->GetStringUTFChars(env, jstr, NULL);
    if(str == NULL){
        return;
    }
    LOGD("%s: str = %s \n",__func__,str);
    (*env)->ReleaseStringUTFChars(env,jstr,str);

    LOGD("%s end \n",__func__);
    doMethod();
}

