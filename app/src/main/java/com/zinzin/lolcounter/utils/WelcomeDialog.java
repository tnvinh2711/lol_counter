package com.zinzin.lolcounter.utils;

import android.app.Activity;
import android.app.Dialog;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zinzin.lolcounter.R;

public class WelcomeDialog extends Dialog {
    private static final String TAG = WelcomeDialog.class.getSimpleName();
    private Activity mActivity;
    private TextView btnStart;
    private ImageView imgHeader;
    private DialogCallBack dialogClickCallBack;

    public WelcomeDialog(Activity activity, DialogCallBack dialogClickCallBack) {
        super(activity);
        this.mActivity = activity;
        this.dialogClickCallBack = dialogClickCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ads);
        btnStart = findViewById(R.id.welcome_btn_start);
        btnStart.setText("Mở");
        imgHeader = findViewById(R.id.img_header_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgHeader.setClipToOutline(true);
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(dialogClickCallBack != null) dialogClickCallBack.onClickOpen();
            }
        });
    }

    public interface DialogCallBack {
        void onClickOpen();
    }
}

