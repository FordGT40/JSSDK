package com.wisdom.jsinterfacelib.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.wisdom.jsinterfacelib.R;

import java.io.File;
import java.io.IOException;


/**
 * Created by Tsing on 2020/8/6
 */
public abstract class SignViewDialog extends Dialog implements View.OnClickListener {

    ImageView ivScreen;
    ImageView iv_close;
    SignatureView signview;
    ImageButton ivClear;
    TextView btnCancel;
    TextView btnSure;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.currentTimeMillis() + "sign.jpg";



    public SignViewDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
        setContentView(R.layout.dialog_sign_view);
        ivScreen=findViewById(R.id.iv_screen);
        signview=findViewById(R.id.signview);
        ivClear=findViewById(R.id.iv_clear);
        btnCancel=findViewById(R.id.btn_cancel);
        btnSure=findViewById(R.id.btn_sure);
        iv_close=findViewById(R.id.iv_close);
        ivScreen.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        iv_close.setOnClickListener(this);

    }




    public abstract void onComplete(String path);

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_close) {
            dismiss();
        } else if (id == R.id.iv_screen) {
            Intent intent = new Intent(getContext(), SignViewActivity.class);
            getContext().startActivity(intent);
        } else if (id == R.id.iv_clear) {
            signview.clear();
        } else if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_sure) {
            if (signview.getTouched()) {
                try {
                    signview.save(path, true, 10);
                    onComplete(signview.getSavePath());
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
