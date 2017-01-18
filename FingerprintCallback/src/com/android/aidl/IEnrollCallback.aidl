package com.android.aidl;


interface IEnrollCallback{

 		void onCaptureCompleted();

        void onCaptureFailed(int code);

        void onProgress(int num);

        void onEnrolled(int fid);

        void onEnrollmentFailed();

        void onFingerRemoved();

}