package com.linhnv.foodsy.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

/**
 * Created by linhnv on 08/07/2017.
 */

public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false); //can cancel when press back
            mProgressDialog.setMessage("Loading");
            if (!mProgressDialog.isShowing()){
                mProgressDialog.show();
            }

        }
    }
    public void showProgressDialog(String msg){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false); //can cancel when press back
            mProgressDialog.setMessage(msg);
            if (!mProgressDialog.isShowing()){
                mProgressDialog.show();
            }
        }
    }
    public void hideProgressDialog(){

        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
