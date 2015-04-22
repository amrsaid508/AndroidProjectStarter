
package com.deardhruv.projectstarter.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/** Helper methods that are used all over the app. */
public final class Helper {
	private static final String LOGTAG = "Helper";
	private static final Logger LOG = new Logger(LOGTAG);

	public static final long LOCATION_TIMEOUT = 15 * 60 * 1000;

	static final String DATE_TEMPLATE = "EEE, d MMM yyyy HH:mm:ss Z";

	private static final long ONE_DAY = 86400L;
	public static final long FOUR_WEEKS = 28L * ONE_DAY * 1000L;

	public static final SimpleDateFormat FORMATTER1 = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	public static final SimpleDateFormat FORMATTER2 = new SimpleDateFormat("dd.MM.yyyy",
			Locale.getDefault());
	public static final SimpleDateFormat FORMATTER3 = new SimpleDateFormat("HH:mm",
			Locale.getDefault());
	public static final SimpleDateFormat ZULU_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

	public Helper() {
		// should not be instantiated
	}

	/**
	 * Check for active internet connection
	 *
	 * @param ctx
	 * @return
	 */
	public static boolean isInternetActive(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conMgr.getActiveNetworkInfo();

		return info != null && info.isConnected() && info.isAvailable();
	}

	public static void closeCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			c.close();
		}
	}

	public static String getAdAgeInDays(final String adDate) throws ParseException {
		return "" + getDaysBetweenDates(FORMATTER1.parse(adDate), new Date());
	}

	/**
	 * @param date1 -past date
	 * @param date2 -future date than date1
	 * @return total days between Dates
	 */
	public static int getDaysBetweenDates(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24l));
	}

	public static String getCurrentDate() {
		return FORMATTER1.format(new Date());
	}

	/**
	 * Returns a semicolon separated list built from the passed list of String.
	 *
	 * @param list A list of strings.
	 * @return A semicolon separated list in one String object or an empty
	 *         string if the passed list is null.
	 */
	private static String createSemicolonSeparatedList(List<String> list) {
		if (list == null)
			return "";

		String semicolonSeparatedList = "";
		int i = 0;

		for (String item : list) {
			semicolonSeparatedList += item;

			if (list.size() > i + 1) {
				semicolonSeparatedList += ";";
			}

			i++;
		}

		return semicolonSeparatedList;
	}

	public static String getFilenameFromPath(String path) {
		if (path == null) {
			throw new IllegalArgumentException("path cannot be null");
		}
		int lastIndexOf = path.lastIndexOf("/");
		if (lastIndexOf == -1) {
			return path;
		}
		return path.substring(lastIndexOf + 1, path.length());
	}

	public static void hideKeyboard(Context context, View view) {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		if (view == null) {
			throw new IllegalArgumentException("view cannot be null");
		}
		InputMethodManager mgr = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static boolean isClickedOutsideOf(MotionEvent me, View v) {
		int[] location = {
				0, 0
		};
		v.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];

		if (me.getX() < x) {
			return true;
		} else if (me.getY() < y) {
			return true;
		} else if (me.getX() > x + v.getMeasuredWidth()) {
			return true;
		} else if (me.getY() > y + v.getMeasuredHeight()) {
			return true;
		}
		return false;
	}

	public static String getLocationTextForDetail(String country, String cityzip, String city) {
		return country + "-" + cityzip + " " + city;
	}

	private static boolean isOnline(Context ctx) {
		final ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}

	/**
	 * Check if roaming connection active.
	 *
	 * @return
	 */
	public static boolean isConnectionRoaming(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		return ni != null && ni.isRoaming();

	}

	/**
	 * Is Roaming enabled.
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isRoamingEnabled(Context ctx) {
		ContentResolver cr = ctx.getContentResolver();
		int result = 0; // 0 is false
		boolean check = false;

		try {
			result = Settings.Secure.getInt(cr, Settings.Secure.DATA_ROAMING);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}

		if (result == 1) {
			check = true;
		}

		return check;
	}

	/**
	 * Check if network connection is allowed.
	 *
	 * @return
	 */
	public static boolean isNetworkConnectionAllowed(Context ctx) {
		boolean result;
		result = isOnline(ctx)
				&& (isConnectionRoaming(ctx) && isRoamingEnabled(ctx) || !isConnectionRoaming(ctx));

		if (!result) {
			LOG.w("no network connection allowed");
		}

		return result;
	}

	public static boolean isAppInstalled(String uri, Context context) {
		PackageManager pm = context.getPackageManager();
		boolean app_installed;

		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}

		return app_installed;
	}

	public static List<NameValuePair> encodeNameValuePair(Bundle parameters) {
		List<NameValuePair> nameValuePair = new ArrayList<>();

		if (parameters != null && parameters.size() > 0) {
			boolean first = true;
			for (String key : parameters.keySet()) {
				if (key != null) {
					if (first) {
						first = false;
					}
					String value = "";
					Object object = parameters.get(key);
					if (object != null) {
						value = String.valueOf(object);
					}
					try {
						nameValuePair.add(new BasicNameValuePair(key, value));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return nameValuePair;
	}

	@SuppressWarnings("deprecation")
	public static String encodeGETUrl(Bundle parameters) {
		StringBuilder sb = new StringBuilder();

		if (parameters != null && parameters.size() > 0) {
			boolean first = true;
			for (String key : parameters.keySet()) {
				if (key != null) {

					if (first) {
						first = false;
					} else {
						sb.append("&");
					}
					String value = "";
					Object object = parameters.get(key);
					if (object != null) {
						value = String.valueOf(object);
					}

					try {
						sb.append(URLEncoder.encode(key, HTTP.UTF_8)).append("=")
								.append(URLEncoder.encode(value, HTTP.UTF_8));
					} catch (Exception e) {
						sb.append(URLEncoder.encode(key)).append("=")
								.append(URLEncoder.encode(value));
					}
				}
			}
		}
		return sb.toString();
	}

	public static String encodeUrl(String url, Bundle mParams) {
		return url + encodeGETUrl(mParams);
	}

	/**
	 * Writes long strings to the logcat.
	 *
	 * @param str The string to write to the logcat.
	 */
	public static void logLongStrings(String logTag, String str) {
		if (str.length() > 4000) {
			Log.d(logTag, str.substring(0, 4000));
			logLongStrings(logTag, str.substring(4000));
		} else {
			Log.d(logTag, str);
		}
	}

	public static StringBuffer getMd5Checksum(String fileContentBaseEncoded) {
		byte[] bytesOfMessage = fileContentBaseEncoded.getBytes();
		return getMd5Checksum(bytesOfMessage);
	}

	public static StringBuffer getMd5Checksum(byte[] bytesOfMessage) {
		byte[] md5sum;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md5sum = md.digest(bytesOfMessage);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e.getMessage());
		}

		StringBuffer md5checksum = new StringBuffer();

		for (byte aMd5sum : md5sum) {
			md5checksum.append(Integer.toString((aMd5sum & 0xff) + 0x100, 16).substring(1));
		}

		return md5checksum;
	}

	/**
	 * Returns the current app version code.
	 *
	 * @param ctx The Android application context.
	 * @return Application's version code from the {@link PackageManager}.
	 */
	public static int getAppVersion(Context ctx) {
		try {
			PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
					0);

			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * @param ctx The Android application context.
	 * @return Application name
	 */
	public static String getAppName(Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager
					.getApplicationInfo(ctx.getApplicationInfo().packageName, 0);
		} catch (final NameNotFoundException e) {
		}
		return (String) (applicationInfo != null
				? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
	}

	/**
	 * Tries to open the app store by using the passed storeAppUri. If this
	 * fails, opens the store website.
	 *
	 * @param ctx The Android context.
	 * @param storeAppUri The app store uri.
	 * @param storeWebsiteUri The store website.
	 */
	public static void openAppStore(Context ctx, Uri storeAppUri, Uri storeWebsiteUri) {
		Intent marketIntent;

		try {
			marketIntent = new Intent(Intent.ACTION_VIEW, storeAppUri);
			marketIntent.addFlags(
					Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			ctx.startActivity(marketIntent);

		} catch (android.content.ActivityNotFoundException anfe) {
			marketIntent = new Intent(Intent.ACTION_VIEW, storeWebsiteUri);
			marketIntent.addFlags(
					Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			ctx.startActivity(marketIntent);
		}
	}

	/**
	 * @return true If the current system OS version is higher then HONEYCOMB[11]
	 */
	public static boolean isHoneyCombOrHigher() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * Show the activity over the lockscreen and wake up the device. If you
	 * launched the app manually both of these conditions are already true. If
	 * you deployed from the IDE, however, this will save you from hundreds of
	 * power button presses and pattern swiping per day!
	 */
	public static void riseAndShine(Activity activity) {
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

		PowerManager power = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock lock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wakeup!");

		lock.acquire();
		lock.release();
	}
}
