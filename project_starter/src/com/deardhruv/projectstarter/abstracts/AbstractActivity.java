
package com.deardhruv.projectstarter.abstracts;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.deardhruv.projectstarter.utils.Logger;

/**
 * @author DearDhruv
 */
public abstract class AbstractActivity extends AppCompatActivity { //FragmentActivity {

    private static final String LOGTAG = "AbstractActivity";
    private static final Logger LOG = new Logger(LOGTAG);

    protected void showToast(String msg) {
        Toast.makeText(AbstractActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
