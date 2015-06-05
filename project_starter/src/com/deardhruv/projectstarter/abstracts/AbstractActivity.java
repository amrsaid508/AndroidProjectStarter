
package com.deardhruv.projectstarter.abstracts;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.utils.Logger;
import com.rey.material.widget.SnackBar;

/**
 * @author DearDhruv
 */
public abstract class AbstractActivity extends AppCompatActivity { //FragmentActivity {

    private static final String LOGTAG = "AbstractActivity";
    private static final Logger LOG = new Logger(LOGTAG);
    private SnackBar snackBar;

    protected void showMsg(String msg) {
        if (snackBar != null) {
            showSnackBarMsg(msg);
        } else {
            showToastMsg(msg);
        }
    }

    protected void showToastMsg(String msg) {
        Toast.makeText(AbstractActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    protected void initSnackBar(Context context) {
        snackBar = SnackBar.make(context).applyStyle(R.style.SnackBarSingleLine);
        snackBar.singleLine(true)
                .actionText("CLOSE")
                .actionClickListener(new SnackBar.OnActionClickListener() {
                    @Override
                    public void onActionClick(SnackBar snackBar, int i) {
                        snackBar.dismiss();
                    }
                })
                .backgroundColor(Color.parseColor("#5A5A5A"))
                .textColor(Color.parseColor("#FFFFFF"))
                .actionTextColor(Color.parseColor("#FFFFFF"))
                .duration(5000);
    }

    protected void showSnackBarMsg(String msg) {
        if (snackBar != null) {
            snackBar.text(msg);
            snackBar.show(AbstractActivity.this);
        }
    }

}
