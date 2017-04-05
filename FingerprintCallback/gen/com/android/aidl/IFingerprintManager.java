/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/fht/work/workman/gitHub/zprojects/FingerprintCallback/src/com/android/aidl/IFingerprintManager.aidl
 */
package com.android.aidl;
public interface IFingerprintManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.aidl.IFingerprintManager
{
private static final java.lang.String DESCRIPTOR = "com.android.aidl.IFingerprintManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.aidl.IFingerprintManager interface,
 * generating a proxy if needed.
 */
public static com.android.aidl.IFingerprintManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.aidl.IFingerprintManager))) {
return ((com.android.aidl.IFingerprintManager)iin);
}
return new com.android.aidl.IFingerprintManager.Stub.Proxy(obj);
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
case TRANSACTION_setOnAuthenticateListen:
{
data.enforceInterface(DESCRIPTOR);
com.android.aidl.IAuthenticateCallback _arg0;
_arg0 = com.android.aidl.IAuthenticateCallback.Stub.asInterface(data.readStrongBinder());
this.setOnAuthenticateListen(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setOnEnrollListen:
{
data.enforceInterface(DESCRIPTOR);
com.android.aidl.IEnrollCallback _arg0;
_arg0 = com.android.aidl.IEnrollCallback.Stub.asInterface(data.readStrongBinder());
this.setOnEnrollListen(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_authenticate:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.authenticate();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_preEnroll:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.preEnroll();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_enroll:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.enroll();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_postEnroll:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.postEnroll();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_stopAuthenticate:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.stopAuthenticate();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_remove:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.remove(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_enumerate:
{
data.enforceInterface(DESCRIPTOR);
int[] _arg0;
_arg0 = data.createIntArray();
int _arg1;
_arg1 = data.readInt();
int _result = this.enumerate(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
reply.writeIntArray(_arg0);
return true;
}
case TRANSACTION_resetVolt:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.resetVolt();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.aidl.IFingerprintManager
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
@Override public void setOnAuthenticateListen(com.android.aidl.IAuthenticateCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setOnAuthenticateListen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setOnEnrollListen(com.android.aidl.IEnrollCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setOnEnrollListen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int authenticate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_authenticate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int preEnroll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_preEnroll, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int enroll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enroll, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int postEnroll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_postEnroll, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int stopAuthenticate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopAuthenticate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int remove(int fid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(fid);
mRemote.transact(Stub.TRANSACTION_remove, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int enumerate(int[] dat, int N) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeIntArray(dat);
_data.writeInt(N);
mRemote.transact(Stub.TRANSACTION_enumerate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readIntArray(dat);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int resetVolt() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resetVolt, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setOnAuthenticateListen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setOnEnrollListen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_authenticate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_preEnroll = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_enroll = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_postEnroll = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_stopAuthenticate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_remove = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_enumerate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_resetVolt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
}
public void setOnAuthenticateListen(com.android.aidl.IAuthenticateCallback callback) throws android.os.RemoteException;
public void setOnEnrollListen(com.android.aidl.IEnrollCallback callback) throws android.os.RemoteException;
public int authenticate() throws android.os.RemoteException;
public int preEnroll() throws android.os.RemoteException;
public int enroll() throws android.os.RemoteException;
public int postEnroll() throws android.os.RemoteException;
public int stopAuthenticate() throws android.os.RemoteException;
public int remove(int fid) throws android.os.RemoteException;
public int enumerate(int[] dat, int N) throws android.os.RemoteException;
public int resetVolt() throws android.os.RemoteException;
}
