package com.wisdom.jsinterfacelib.weight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wisdom.jsinterfacelib.utils.ImageUtil;
import com.wisdom.jsinterfacelib.R;

import java.io.File;

/**
 * 签字板 Activity
 */
public class DrawingBoardActivity extends AppCompatActivity {
    public static final int RESULT_CODE_DRAWING = 0x115;
    private Button bt_clear;
    private Button bt_submit;
    private DrawingBoardView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);
        bt_clear = findViewById(R.id.bt_clear);
        bt_submit = findViewById(R.id.bt_submit);
        drawView = findViewById(R.id.drawView);
        String textColor = getIntent().getStringExtra("color");
        if ("".equals(textColor) || textColor == null) {
            textColor = "#000000";
        }
        drawView.setPaintColor(Color.parseColor(textColor));
        drawView.setDrawWidth(4);
        //清空界面
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clearPanel();
            }
        });
        //保存图片到本地
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存签名图片
                Bitmap bitmap = ImageUtil.getViewBitmap(drawView);
                try {
                    File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG);
                    Intent intentBack = new Intent();
                    intentBack.putExtra("path", file.getAbsolutePath());
                    setResult(RESULT_CODE_DRAWING, intentBack);
                    finish();
                } catch (Exception e) {
                    ToastUtils.showShort("系统异常，请稍后重试");

                }

            }
        });


    }


}