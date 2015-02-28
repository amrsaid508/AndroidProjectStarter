
package com.deardhruv.projectstarter.utils;

import android.os.Build;

import com.deardhruv.projectstarter.BuildConfig;

/**
 * Log class wrapper. Shows only log messages, if app is in debug mode!
 */
public class Logger {
	private final boolean showLogMessages;
	private final String tag;

	/**
	 * Constructor which requires a logtag for the Log messages. Log messages
	 * are always shown in Debug mode.
	 * 
	 * @param tag - Logtag of the class
	 */
	public Logger(final String tag) {
		this(tag, BuildConfig.DEBUG);
	}

	/**
	 * Constructor which requires a logtag for the log messages and a flag if
	 * the log messages should be shown or not
	 * 
	 * @param tag - Logtag of the class
	 * @param showAlwaysLogMessages - enable or disable log messages
	 */
	public Logger(final String tag, final boolean showAlwaysLogMessages) {
		this.tag = tag;
		this.showLogMessages = showAlwaysLogMessages;
	}

	/**
	 * {@link android.utils.Log.d}
	 */
	public void d(final String msg) {
		if (showLogMessages) {
			android.util.Log.d(tag, msg);
		}
	}

	/**
	 * {@link android.utils.Log.d}
	 */
	public void d(final String msg, Throwable t) {
		if (showLogMessages) {
			android.util.Log.d(tag, msg, t);
		}
	}

	/**
	 * {@link android.utils.Log.e}
	 */
	public void e(final String msg) {
		if (showLogMessages) {
			android.util.Log.e(tag, msg);
		}
	}

	/**
	 * {@link android.utils.Log.t}
	 */
	public void e(final String msg, Throwable t) {
		if (showLogMessages) {
			android.util.Log.e(tag, msg, t);
		}
	}

	/**
	 * {@link android.utils.Log.i}
	 */
	public void i(final String msg) {
		if (showLogMessages) {
			android.util.Log.i(tag, msg);
		}
	}

	/**
	 * {@link android.utils.Log.i}
	 */
	public void i(final String msg, Throwable t) {
		if (showLogMessages) {
			android.util.Log.i(tag, msg, t);
		}
	}

	/**
	 * {@link android.utils.Log.w}
	 */
	public void w(final String msg) {
		if (showLogMessages) {
			android.util.Log.w(tag, msg);
		}
	}

	/**
	 * {@link android.utils.Log.w}
	 */
	public void w(final String msg, Throwable t) {
		if (showLogMessages) {
			android.util.Log.w(tag, msg, t);
		}
	}

	/**
	 * {@link android.utils.Log.wtf}
	 */
	public void wtf(final String msg) {
		if (showLogMessages) {
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				android.util.Log.wtf(tag, msg);
			} else {
				android.util.Log.e(tag, msg);
			}
		}
	}

	/**
	 * {@link android.utils.Log.t}
	 */
	public void wtf(Throwable t) {
		if (showLogMessages) {
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				android.util.Log.wtf(tag, t);
			} else {
				android.util.Log.e(tag, t.getMessage(), t);
			}
		}
	}

	/**
	 * {@link android.utils.Log.wtf}
	 */
	public void wtf(final String msg, Throwable t) {
		if (showLogMessages) {
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				android.util.Log.wtf(tag, msg, t);
			} else {
				android.util.Log.e(tag, msg, t);
			}
		}
	}
}
