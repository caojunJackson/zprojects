package ma.release;

import android.R.integer;
import android.util.Log;

public class Jnifp {
    String TAG = "ATAG";
    Fingerprint mFingerprint= Fingerprint.getInstance();
    
    static{
        System.loadLibrary("mafprint");
    }
    
    /*
     * Jni回调通知数据更新
     */
    //notify
    public void callback(Fingerprint fp){
        Log.i(TAG, "====================callback======");
        Log.i(TAG, "======callback == from jni==" +
        		" result="+fp.result+", " +"msg = "+fp.msg+" ,fid = "+fp.fid+" , ecode = "+fp.ecode);
        
        
        switch (fp.msg) {
            case Fingerprint.MSG_ENROLL:
                switch (fp.ecode) {
                    case Fingerprint.FP_CHK_FULL:
                        mFingerprint.mEnrollCallback.onCaptureCompleted();
                        break;
                    case Fingerprint.FP_OK:
                        mFingerprint.mEnrollCallback.onProgress(fp.result);
                        if(fp.result == 100){
                            mFingerprint.mEnrollCallback.onEnrolled(fp.fid);
                        }
                        break;
                    case Fingerprint.FP_ENROLL_FAIL:
                        mFingerprint.mEnrollCallback.onEnrollmentFailed();
                        break;
                    case Fingerprint.FP_CHK_PART:
                        mFingerprint.mEnrollCallback.onCaptureFailed(Fingerprint.FP_CHK_PART);
                        break;
                    case Fingerprint.FP_TOUCH_TOO_SHORT:
                        mFingerprint.mEnrollCallback.onCaptureFailed(Fingerprint.FP_TOUCH_TOO_SHORT);
                    case Fingerprint.FP_DUPLI_FINGER:
                        mFingerprint.mEnrollCallback.onCaptureFailed(Fingerprint.FP_DUPLI_FINGER);
                        break;
                    case Fingerprint.FP_DUPLI_AREA:
                        mFingerprint.mEnrollCallback.onCaptureFailed(Fingerprint.FP_DUPLI_AREA);
                        break;
                    case Fingerprint.FP_CHK_UP:
                        mFingerprint.mEnrollCallback.onFingerRemoved();
                        break;
                    case Fingerprint.FP_CHK_DOWN:
                        
                        break;
                    default:
                        break;
                }
               
                break;
            case Fingerprint.MSG_MATCH:
                Log.i(TAG, "=========Jnifp===MSG_MATCH======"+fp.ecode);
                if(Fingerprint.FP_OK == fp.ecode){
                    mFingerprint.mAuthenticateCallback.onIdentified(fp.result);
                    
                }else if(Fingerprint.FP_MATCH_FAIL ==fp.ecode){
                    mFingerprint.mAuthenticateCallback.onCaptureFailed(Fingerprint.FP_MATCH_FAIL);
                    
                }else if(Fingerprint.FP_MATCH_NO_FINGER == fp.ecode){
                    mFingerprint.mAuthenticateCallback.onNoMatch();
                }else if(Fingerprint.FP_CHK_UP == fp.ecode){
                    mFingerprint.mAuthenticateCallback.onFingerRemoved();
                }else if(Fingerprint.FP_CHK_DOWN == fp.ecode){
                    
                }
                break;

            case Fingerprint.MSG_REMOVE:
                Log.i(TAG, "=========Jnifp===MSG_REMOVE======");
                break;

            case Fingerprint.MSG_IDLE:
                Log.i(TAG, "=========Jnifp===MSG_IDLE======");
                break;

            case Fingerprint.MAG_TEST_START:
                Log.i(TAG, "=========Jnifp===MAG_TEST_START======");
                break;

            case Fingerprint.MAG_TEST_STOP:
                Log.i(TAG, "=========Jnifp===MAG_TEST_STOP======");
                break;

            case Fingerprint.MSG_CALIBRATE:
                Log.i(TAG, "=========Jnifp===MSG_CALIBRATE======");
                break;
            default:
                break;
        }
    }
    
   /*
    * Jni  回调设置Fingerprint msg 的值
    */
    public void setMsg(int msg){
        mFingerprint.msg = msg;
    }
    
    /*
     * Jni  回调设置Fingerprint fid 的值
     */
    public void setFid(int fid){
        mFingerprint.fid = fid;
    }
    
    /*
     * Jni  回调设置Fingerprint result 的值
     */
    public void setResult(int result){
        mFingerprint.result = result;
    }
    
    /*
     * Jni  回调设置Fingerprint ecode 的值
     */
    public void setEcode(int ecode){
        mFingerprint.ecode = ecode;
    }
    
    
    public static native int open(String path);    //开启设备       
    
    public static native int close();       //关闭设备
    
    public static native int calibrate();   //指纹校准
    
    public static native int preEnroll();   //注册前
    
    public static native int enroll();      //注册
    
    public static native int postEnroll();  //注册结束
    
    public static native int authenticate(); //匹配
    
    public static native int stopAuthenticate(); //停止匹配
    
    public static native int enumerate(byte[] dat, int N);       
    public static native int remove(int fid);   //移除指纹
    
    public static native int setDetectMode(int mode); //匹配解锁分开亮屏，灭屏
    
    public static native int setKeyMode(int mode); //设置key模式
        
    public native void setNotify(); //初始化回调方法，在开启指纹功能时要设置
    
}
