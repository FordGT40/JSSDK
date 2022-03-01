package com.wisdom.jsinterfacelib.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


import com.wisdom.jsinterfacelib.R;


import java.io.File;
import java.util.ArrayList;


/**
 * Created by Tsing on 2020/8/3
 */
public class ImagePreviewActivityJs extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewpager;
    TextView tv_indicator;
    ProgressBar progressBar;
    ImageButton ib;
    ImageButton ic_rotate;
    ImageView iv_back;

    private ArrayList<String> imgs;
    private boolean download;
    private boolean rotate;
    private boolean local = false;
    private int index;
    private ImageAdapter adapter;
    private float rotation = 0;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_previewjs);
        initView();
    }

    public void initView() {
        viewpager = findViewById(R.id.viewpager);
        tv_indicator = findViewById(R.id.tv_indicator);
        progressBar = findViewById(R.id.progressBar);
        ib = findViewById(R.id.ib);
        ic_rotate = findViewById(R.id.ic_rotate);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        ic_rotate.setOnClickListener(this);
        ib.setOnClickListener(this);


        index = getIntent().getIntExtra("index", 0);
        download = getIntent().getBooleanExtra("download", false);
        rotate = getIntent().getBooleanExtra("rotate", false);
        imgs = getIntent().getStringArrayListExtra("imgs");


        tv_indicator.setVisibility(View.VISIBLE);
        if (download) {
            ib.setVisibility(View.VISIBLE);
        } else {
            ib.setVisibility(View.GONE);
        }
        if (rotate) {
            ic_rotate.setVisibility(View.VISIBLE);
        } else {
            ic_rotate.setVisibility(View.GONE);
        }


        tv_indicator.setText((index + 1) + "/" + imgs.size());


        adapter = new ImageAdapter();
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(index);
        viewpager.setOffscreenPageLimit(imgs.size());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                rotation = 0;
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_indicator.setText((position + 1) + "/" + imgs.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static void goImagePreview(Context context, boolean download, boolean rotate, int index, ArrayList<String> imgs) {
        Intent intent = new Intent(context, ImagePreviewActivityJs.class);
        intent.putExtra("index", index);
        intent.putExtra("download", download);
        intent.putExtra("rotate", rotate);
        intent.putStringArrayListExtra("imgs", imgs);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.ic_rotate) {
            PhotoView photoView = viewpager.findViewWithTag(viewpager.getCurrentItem());
            photoView.setRotation(rotation -= 90);
        } else if (id == R.id.ib) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(local ? new File(imgs.get(index)) : imgs.get(index))
                    .into(new SimpleTarget<Bitmap>() {
                              @Override
                              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                  ImageUtils.save2Album(resource, Bitmap.CompressFormat.JPEG);
                                  runOnUiThread(() -> Toast.makeText(ImagePreviewActivityJs.this,"图片保存成功,请到相册查看",Toast.LENGTH_SHORT).show());
                              }

                              @Override
                              public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                  super.onLoadFailed(errorDrawable);
                                  runOnUiThread(() -> Toast.makeText(ImagePreviewActivityJs.this,"图片保存失败",Toast.LENGTH_SHORT).show());
                              }

                              @Override
                              public void onLoadStarted(@Nullable Drawable placeholder) {
                                  super.onLoadStarted(placeholder);

                              }
                          }
                    );
        }
    }

    class ImageAdapter extends PagerAdapter {


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView view = new PhotoView(ImagePreviewActivityJs.this);
            view.enable();
            view.setId(R.id.img_icon);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(local ? new File(imgs.get(position)) : imgs.get(position))
                    .into(new SimpleTarget<Bitmap>() {
                              @Override
                              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                  view.setImageBitmap(resource);
                                  if (progressBar != null) {
                                      progressBar.setVisibility(View.GONE);
                                  }
                              }

                              @Override
                              public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                  super.onLoadFailed(errorDrawable);
                                  if (progressBar != null) {
                                      progressBar.setVisibility(View.GONE);
                                  }
                                  ToastUtils.showShort("图片加载失败");
                              }

                              @Override
                              public void onLoadStarted(@Nullable Drawable placeholder) {
                                  super.onLoadStarted(placeholder);
                                  if (progressBar != null) {
                                      progressBar.setVisibility(View.VISIBLE);
                                  }
                              }
                          }
                    );

            container.addView(view);
            view.setTag(position);
            view.setOnClickListener(v -> finish());
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }


}
