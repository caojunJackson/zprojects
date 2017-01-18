// IMediaplayInterface.aidl
package com.android.aidl;

// Declare any non-default types here with import statements
// Mediaplay fun
interface IMediaplayInterface {
        boolean isplay();
        void prepare();
        void pause();
        void start();
        void stop();
        int getDuration();
        void seekTo(int position);
        int getCurrentPosition();
        void looper();
        void preparePlay(String path);
        int getMusciPostionAtList(String path);
}
