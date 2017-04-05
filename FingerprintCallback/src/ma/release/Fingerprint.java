package ma.release;

public class Fingerprint {
    /*
     * 回调返回msg类型
     */
    public static final int MSG_IDLE = 0;
    public static final int MSG_ENROLL = 1;
    public static final int MSG_MATCH = 2;  
    public static final int MSG_REMOVE = 3;
    public static final int MSG_CALIBRATE = 4;
    public static final int MAG_TEST_START = 5;
    public static final int MAG_TEST_STOP = 6;
    
    /*
     * 回调返回ecode
     */
    public static final int FP_CANCEL       =8;     //取消
    public static final int FP_CHK_FULL     = 3;   //全部接触
    public static final int FP_CHK_PART     = 2;    //部分接触
    public static final int FP_CHK_DOWN     = 1;     //手指按下
    public static final int FP_CHK_UP       = 5;    //手指离开
    public static final int FP_OK           = 0;    //成功/完成
    public static final int FP_ENROLL_FAIL  = -1;    //注册失败
    public static final int FP_MATCH_FAIL   = -2;    //匹配失败
    public static final int FP_DUPLI_AREA   = -40;     //重复区域
    public static final int FP_DUPLI_FINGER = -41;     //重复手指
    public static final int FP_MATCH_NO_FINGER = -42;  //没有录入指纹 
    public static final int FP_TOUCH_TOO_SHORT = -43;    //接触时间太短
    public int msg;
    public int fid;
    public int result;
    public int ecode;
    
    public IAuthenticateCallback mAuthenticateCallback;
    public IEnrollCallback mEnrollCallback;
    
    
    /*
     * 单例
     */
    private Fingerprint(){}
    private volatile static Fingerprint fingerprint;
    public static Fingerprint getInstance(){
        if(fingerprint == null){
            synchronized (Fingerprint.class) {
                if (fingerprint == null) {
                    fingerprint = new Fingerprint();
                }
            }
        }
        return fingerprint;
    }

    //测试用
    public void doEnroll(){
        mEnrollCallback.onCaptureCompleted();
    }

    public void setOnAuthenticateListen(IAuthenticateCallback callback){
        mAuthenticateCallback = callback;
    }
    
    public void setOnEnrollListen(IEnrollCallback callback){
        mEnrollCallback = callback;
    }
    
    
    
    
    
    
    public static abstract class IAuthenticateCallback{
        public void onNoMatch(){}
        
        public void onIdentified(int fid) {}
      
        public void onCaptureFailed(int reason) {}
       
        public void onFingerRemoved() {}

    }
    
    
    public static abstract class IEnrollCallback{
        public void onCaptureCompleted() {}

        public void onCaptureFailed(int code) {}

        public void onProgress(int num) {}

        public void onEnrolled(int fid) {}

        public void onEnrollmentFailed() {}

        public void onFingerRemoved() {}

    }
}
