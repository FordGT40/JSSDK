package com.wisdom.jsinterfacelib.weight;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.wisdom.jsinterfacelib.R;
import com.wisdom.jsinterfacelib.model.SignEvent;

import androidx.annotation.Nullable;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Tsing on 2020/8/6
 */
public class SignViewActivity extends Activity implements View.OnClickListener {
    ImageView ivScreen;
    ImageView iv_close;
    SignatureView signview;
    ImageButton ivClear;
    TextView btnCancel;
    TextView btnSure;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sign_view);
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



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_close || id == R.id.iv_screen || id == R.id.btn_cancel) {
            finish();
        } else if (id == R.id.iv_clear) {
            signview.clear();
        } else if (id == R.id.btn_sure) {
            if (signview.getTouched()) {
                try {
                    signview.save(SignViewDialog.path, true, 10);
                    EventBus.getDefault().post(new SignEvent(signview.getSavePath()));
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
