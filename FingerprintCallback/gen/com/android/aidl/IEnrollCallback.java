/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/fht/work/workman/gitHub/zprojects/FingerprintCallback/src/com/android/aidl/IEnrollCallback.aidl
 */
package com.android.aidl;
public interface IEnrollCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.aidl.IEnrollCallback
{
private static final java.lang.String DESCRIPTOR = "com.android.aidl.IEnrollCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.aidl.IEnrollCallback interface,
 * generating a proxy if needed.
 */
public static com.android.aidl.IEnrollCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.aidl.IEnrollCallback))) {
return ((com.android.aidl.IEnrollCallback)iin);
}
return new com.android.aidl.IEnrollCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onCaptureCompleted:
{
data.enforceInterface(DESCRIPTOR);
this.onCaptureCompleted();
reply.writeNoException();
return true;
}
case TRANSACTION_onCaptureFailed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onCaptureFailed(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onProgress:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onProgress(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onEnrolled:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onEnrolled(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onEnrollmentFailed:
{
data.enforceInterface(DESCRIPTOR);
this.onEnrollmentFailed();
reply.writeNoException();
return true;
}
case TRANSACTION_onFingerRemoved:
{
data.enforceInterface(DESCRIPTOR);
this.onFingerRemoved();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.aidl.IEnrollCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onCaptureCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onCaptureCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCaptureFailed(int code) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(code);
mRemote.transact(Stub.TRANSACTION_onCaptureFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onProgress(int num) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(num);
mRemote.transact(Stub.TRANSACTION_onProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onEnrolled(int fid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(fid);
mRemote.transact(Stub.TRANSACTION_onEnrolled, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onEnrollmentFailed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onEnrollmentFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onFingerRemoved() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onFingerRemoved, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onCaptureCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onCaptureFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onEnrolled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onEnrollmentFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onFingerRemoved = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void onCaptureCompleted() throws android.os.RemoteException;
public void onCaptureFailed(int code) throws android.os.RemoteException;
public void onProgress(int num) throws android.os.RemoteException;
public void onEnrolled(int fid) throws android.os.RemoteException;
public void onEnrollmentFailed() throws android.os.RemoteException;
public void onFingerRemoved() throws android.os.RemoteException;
}
