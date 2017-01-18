package com.android.aidl;

import com.android.aidl.IEnrollCallback;
import com.android.aidl.IAuthenticateCallback;


interface IFingerprintManager{
	
	void setOnAuthenticateListen(IAuthenticateCallback callback);
	
	void setOnEnrollListen(IEnrollCallback callback);

	int authenticate();
	
	int enroll();
	
	int stopAuthenticate();
	
	int remove(int fid);
	
	int enumerate( inout byte[] dat, in int N);
}