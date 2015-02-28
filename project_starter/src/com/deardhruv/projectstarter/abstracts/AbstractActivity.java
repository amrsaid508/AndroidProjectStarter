
package com.deardhruv.projectstarter.abstracts;

import android.app.Activity;
import android.widget.Toast;

import com.deardhruv.projectstarter.utils.Logger;

/**
 * @author DearDhruv
 */
public abstract class AbstractActivity extends Activity {

	private static final String LOGTAG = "AbstractActivity";
	private static final Logger LOG = new Logger(LOGTAG);

	
	public void showToast(String msg) {
		Toast.makeText(AbstractActivity.this, msg, Toast.LENGTH_LONG).show();
	}
}
