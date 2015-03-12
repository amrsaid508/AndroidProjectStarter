
package com.deardhruv.projectstarter.abstracts;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.deardhruv.projectstarter.utils.Logger;

/**
 * @author DearDhruv
 */
public abstract class AbstractActivity extends FragmentActivity{

	private static final String LOGTAG = "AbstractActivity";
	private static final Logger LOG = new Logger(LOGTAG);

	
	public void showToast(String msg) {
		Toast.makeText(AbstractActivity.this, msg, Toast.LENGTH_LONG).show();
	}
}
