package com.android.aidl;

interface IAuthenticateCallback{
	void onNoMatch();
        
    void onIdentified(int fid);
      
    void onCaptureFailed(int reason);
       
    void onFingerRemoved();

}
