package com.linegraph.ui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by hp on 5/23/2016.
 */
public class AppDialog {
    private static ProgressDialog progressDialog;
    // show progress dialog
    public static void showProgress(Context ctx) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Requesting to server");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();}
    //dismiss progress
    public static void hideProgress() {
        if(progressDialog!=null&&progressDialog.isShowing()){progressDialog.dismiss();}
    }
}
