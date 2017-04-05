
package com.android.activity;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.internal.widget.LockPatternUtils;

import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.app.AlarmManager;
//import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class Util {
	public static String getMessage(Exception e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer, true));
		return writer.toString();
	}

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {

		}
	}

	public static String getPath() {
		File dir = new File("/data/data/ma.fprint/databases");
		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (Exception e) {
				print(e.getMessage());
			}
		}
		return dir.toString();
	}

	public static void print(String str) {
		Log.i("JTAG", str);
	}

	public static void vibrate(Context context, long ms) {
		Vibrator v = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		v.vibrate(ms);
	}

	public static void writeXML(Context context, String str, int val) {
		// print("writeXML: val=" + Integer.toString(val));

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(str, val);
		editor.commit();
	}

	public static void writeXML(Context context, String str, String val) {
		// print("writeXML: val=" + Integer.toString(val));

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(str, val);
		editor.commit();
	}

	public static void writeXML(Context context, String str, boolean val) {
		// print("writeXML: val=" + Integer.toString(val));

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(str, val);
		editor.commit();
	}

	public static int readXML(Context context, String str, int def) {
		int ret;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		ret = sp.getInt(str, def);

		// print("readXML ret=" + Integer.toString(ret));

		return ret;
	}

	public static String readXML(Context context, String str, String def) {
		String ret;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		ret = sp.getString(str, def);

		return ret;
	}

	public static boolean readXML(Context context, String key, boolean def) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, def);
	}

	public static void removeXML(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public static boolean containsXML(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.contains(key);
	}

	public static void Vibrate(final Context con, long milliseconds) {
		Vibrator vib = (Vibrator) con.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	public final static boolean isScreenLocked(Context c) {
		android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	// move from fprintlib.jar begin
	public final static int maxCLength = 4;
	public final static int maxELength = 16;

	public static class FingerKey {
		public static String fid = "fid";
		public static String img = "img";
		public static String name = "name";
	}

	public final static boolean isScreenOn(Context c) {
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	public static void setAlarm(Context context, int gap, PendingIntent sender) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + gap * 60 * 1000, gap * 60 * 1000, sender);
	}

	public static void cancelAlarm(Context context, PendingIntent sender) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
	}

	static PowerManager.WakeLock wl;

	public static void getPowerLock(Context context) {
		if (wl == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "authentication.wake_lock");
		}
		if (!wl.isHeld())
			wl.acquire();
	}

	public static void releasePowerLock() {
		if (wl != null && wl.isHeld())
			wl.release();
	}

	public static long readScreenLockTime(Context context) {
		long nRet = 0;
		if (context != null) {
			nRet = Settings.Secure.getLong(context.getContentResolver(), "lock_screen_lock_after_timeout", 0);
		}
		return nRet;
	}

	public static boolean isFPNeedSleep(Context context) {
		if (readScreenLockTime(context) > 0) {
			return true;
		}
		return false;
	}

	public static boolean isChinese(String str) {
		String regEx1 = "[\\u4e00-\\u9fa5]+";
		String regEx2 = "[\\uFF00-\\uFFEF]+";
		String regEx3 = "[\\u2E80-\\u2EFF]+";
		String regEx4 = "[\\u3000-\\u303F]+";
		String regEx5 = "[\\u31C0-\\u31EF]+";
		Pattern p1 = Pattern.compile(regEx1);
		Pattern p2 = Pattern.compile(regEx2);
		Pattern p3 = Pattern.compile(regEx3);
		Pattern p4 = Pattern.compile(regEx4);
		Pattern p5 = Pattern.compile(regEx5);
		Matcher m1 = p1.matcher(str);
		Matcher m2 = p2.matcher(str);
		Matcher m3 = p3.matcher(str);
		Matcher m4 = p4.matcher(str);
		Matcher m5 = p5.matcher(str);
		if (m1.find() || m2.find() || m3.find() || m4.find() || m5.find())
			return true;
		else
			return false;
	}
	// move from fprintlib.jar end

	/**
	 * 是否指定了特殊的密码
	 */
	public static boolean hasPassword(Context context) {
		LockPatternUtils mLockPatternUtils = new LockPatternUtils(context);
		boolean isPasswordQualityUnspecified = (mLockPatternUtils.getKeyguardStoredPasswordQuality() == 0);
		if (isPasswordQualityUnspecified) {

			return false;
		} else {
			return true;
		}
	}

	public static void putStringSet(Context context, String key, String val) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Set<String> set = sp.getStringSet(key, new HashSet<String>());
		set.add(val);
		SharedPreferences.Editor editor = sp.edit();
		editor.putStringSet(key, set);
		editor.commit();
	}

	public static void saveFingerName(Context context, String fingerName) {
		String key = "finger_name_set";
		putStringSet(context, key, fingerName);
	}

	// byte 与 int 的相互转换
	public static byte intToByte(int x) {
		return (byte) x;
	}

	public static int byteToInt(byte b) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}
	
}
