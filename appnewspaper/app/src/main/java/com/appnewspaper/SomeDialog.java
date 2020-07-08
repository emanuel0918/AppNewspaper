package com.appnewspaper;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class SomeDialog extends DialogFragment {
    private String title;
    private String message;
    private String button;
    private String button2;
    private int opcion;
    private DialogInterface.OnClickListener yesClickListener;
    private DialogInterface.OnClickListener noClickListener;

    public static final int MULTIPLE=1;
    public static final int POSITIVE=2;

    public SomeDialog(){}


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog alertDialog;
        switch(opcion){
            case MULTIPLE:
                alertDialog= new AlertDialog.Builder(getActivity())
                .setTitle(this.title)
                .setMessage(this.message)
                .setNegativeButton(this.button2,this.noClickListener)
                .setPositiveButton(this.button,this.yesClickListener)
                .create();
                break;
            case POSITIVE:
                alertDialog= new AlertDialog.Builder(getActivity())
                        .setTitle(this.title)
                        .setMessage(this.message)
                        .setPositiveButton(this.button,this.yesClickListener)
                        .create();
                break;
            default:
                if(this.title==null)this.title="";
                if(this.message==null)this.message="";
                alertDialog= new AlertDialog.Builder(getActivity())
                        .setTitle(this.title)
                        .setMessage(this.message)
                        .setPositiveButton(android.R.string.ok,this.yesClickListener)
                        .create();
                break;
        }
        return alertDialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPositiveButton(String button,DialogInterface.OnClickListener clickListener) {
        this.button = button;
        this.yesClickListener=clickListener;
    }

    public void setNegativeButton(String button,DialogInterface.OnClickListener clickListener) {
        this.button2 = button;
        this.noClickListener=clickListener;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }
}