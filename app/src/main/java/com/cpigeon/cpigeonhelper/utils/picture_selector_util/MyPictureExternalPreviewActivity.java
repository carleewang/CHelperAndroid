package com.cpigeon.cpigeonhelper.utils.picture_selector_util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.widget.PreviewViewPager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2018/3/3.
 */

public class MyPictureExternalPreviewActivity extends PictureBaseActivity implements View.OnClickListener {
    private ImageButton left_back;
    private TextView tv_title;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList();
    private int position = 0;
    private String directory_path;
    private MyPictureExternalPreviewActivity.SimpleFragmentAdapter adapter;
    private LayoutInflater inflater;
    private RxPermissions rxPermissions;
    private MyPictureExternalPreviewActivity.loadDataThread loadDataThread;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 200:
                    String path = (String)msg.obj;
                    MyPictureExternalPreviewActivity.this.showToast(MyPictureExternalPreviewActivity.this.getString(com.luck.picture.lib.R.string.picture_save_success) + "\n" + path);
                    MyPictureExternalPreviewActivity.this.dismissDialog();
                default:
            }
        }
    };

    public MyPictureExternalPreviewActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(com.luck.picture.lib.R.layout.picture_activity_external_preview);
        this.inflater = LayoutInflater.from(this);
        this.tv_title = (TextView)this.findViewById(com.luck.picture.lib.R.id.picture_title);
        this.left_back = (ImageButton)this.findViewById(com.luck.picture.lib.R.id.left_back);
        this.viewPager = (PreviewViewPager)this.findViewById(com.luck.picture.lib.R.id.preview_pager);
        this.position = this.getIntent().getIntExtra("position", 0);
        this.directory_path = this.getIntent().getStringExtra("directory_path");
        this.images = (List)this.getIntent().getSerializableExtra("previewSelectList");
        this.left_back.setOnClickListener(this);
        this.initViewPageAdapterData();
    }

    private void initViewPageAdapterData() {
        this.tv_title.setText(this.position + 1 + "/" + this.images.size());
        this.adapter = new MyPictureExternalPreviewActivity.SimpleFragmentAdapter();
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setCurrentItem(this.position);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                MyPictureExternalPreviewActivity.this.tv_title.setText(position + 1 + "/" + MyPictureExternalPreviewActivity.this.images.size());
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onClick(View v) {
        this.finish();
        this.overridePendingTransition(0, com.luck.picture.lib.R.anim.a3);
    }

    private void showDownLoadDialog(final String path) {
        final CustomDialog dialog = new CustomDialog(this, ScreenUtils.getScreenWidth(this) * 3 / 4, ScreenUtils.getScreenHeight(this) / 4, com.luck.picture.lib.R.layout.picture_wind_base_dialog_xml, com.luck.picture.lib.R.style.Theme_dialog);
        Button btn_cancel = (Button)dialog.findViewById(com.luck.picture.lib.R.id.btn_cancel);
        Button btn_commit = (Button)dialog.findViewById(com.luck.picture.lib.R.id.btn_commit);
        TextView tv_title = (TextView)dialog.findViewById(com.luck.picture.lib.R.id.tv_title);
        TextView tv_content = (TextView)dialog.findViewById(com.luck.picture.lib.R.id.tv_content);
        tv_title.setText(this.getString(com.luck.picture.lib.R.string.picture_prompt));
        tv_content.setText(this.getString(com.luck.picture.lib.R.string.picture_prompt_content));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyPictureExternalPreviewActivity.this.showPleaseDialog();
                boolean isHttp = PictureMimeType.isHttp(path);
                if(isHttp) {
                    MyPictureExternalPreviewActivity.this.loadDataThread = MyPictureExternalPreviewActivity.this.new loadDataThread(path);
                    MyPictureExternalPreviewActivity.this.loadDataThread.start();
                } else {
                    try {
                        String e = PictureFileUtils.createDir(MyPictureExternalPreviewActivity.this, System.currentTimeMillis() + ".png", MyPictureExternalPreviewActivity.this.directory_path);
                        PictureFileUtils.copyFile(path, e);
                        MyPictureExternalPreviewActivity.this.showToast(MyPictureExternalPreviewActivity.this.getString(com.luck.picture.lib.R.string.picture_save_success) + "\n" + e);
                        MyPictureExternalPreviewActivity.this.dismissDialog();
                    } catch (IOException var4) {
                        MyPictureExternalPreviewActivity.this.showToast(MyPictureExternalPreviewActivity.this.getString(com.luck.picture.lib.R.string.picture_save_error) + "\n" + var4.getMessage());
                        MyPictureExternalPreviewActivity.this.dismissDialog();
                        var4.printStackTrace();
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showLoadingImage(String urlPath) {
        try {
            URL e = new URL(urlPath);
            String path = PictureFileUtils.createDir(this, System.currentTimeMillis() + ".png", this.directory_path);
            byte[] buffer = new byte[8192];
            int ava = 0;
            long start = System.currentTimeMillis();
            BufferedInputStream bin = new BufferedInputStream(e.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path));

            int read;
            while((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                long message = (long)ava / (System.currentTimeMillis() - start);
                DebugUtil.i("Download: " + ava + " byte(s)    avg speed: " + message + "  (kb/s)");
            }

            bout.flush();
            bout.close();
            Message message1 = this.handler.obtainMessage();
            message1.what = 200;
            message1.obj = path;
            this.handler.sendMessage(message1);
        } catch (IOException var13) {
            this.showToast(this.getString(com.luck.picture.lib.R.string.picture_save_error) + "\n" + var13.getMessage());
            var13.printStackTrace();
        }

    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        this.overridePendingTransition(0, com.luck.picture.lib.R.anim.a3);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.loadDataThread != null) {
            this.handler.removeCallbacks(this.loadDataThread);
            this.loadDataThread = null;
        }

    }

    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String path) {
            this.path = path;
        }

        public void run() {
            try {
                MyPictureExternalPreviewActivity.this.showLoadingImage(this.path);
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }
    }

    public class SimpleFragmentAdapter extends PagerAdapter {
        public SimpleFragmentAdapter() {
        }

        public int getCount() {
            return MyPictureExternalPreviewActivity.this.images.size();
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View contentView = MyPictureExternalPreviewActivity.this.inflater.inflate(com.luck.picture.lib.R.layout.picture_image_preview, container, false);
            final PhotoView imageView = (PhotoView)contentView.findViewById(com.luck.picture.lib.R.id.preview_image);
            LocalMedia media = (LocalMedia)MyPictureExternalPreviewActivity.this.images.get(position);
            if(media != null) {
                String pictureType = media.getPictureType();
                final String path;
                if(media.isCut() && !media.isCompressed()) {
                    path = media.getCutPath();
                } else if(!media.isCompressed() && (!media.isCut() || !media.isCompressed())) {
                    path = media.getPath();
                } else {
                    path = media.getCompressPath();
                }

                boolean isHttp = PictureMimeType.isHttp(path);
                if(isHttp) {
                    MyPictureExternalPreviewActivity.this.showPleaseDialog();
                }

                boolean isGif = PictureMimeType.isGif(pictureType);
                if(isGif && !media.isCompressed()) {
                    Glide.with(MyPictureExternalPreviewActivity.this).load(path).asGif().override(480, 800).diskCacheStrategy(DiskCacheStrategy.SOURCE).priority(Priority.HIGH).into(imageView);
                    MyPictureExternalPreviewActivity.this.dismissDialog();
                } else {
                    Glide.with(MyPictureExternalPreviewActivity.this).load(path).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new SimpleTarget(480, 800) {
                        @Override
                        public void onResourceReady(Object resource, GlideAnimation glideAnimation) {

                        }

                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                            MyPictureExternalPreviewActivity.this.dismissDialog();
                        }
                    });
                }

                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    public void onViewTap(View view, float x, float y) {
                        MyPictureExternalPreviewActivity.this.finish();
                        MyPictureExternalPreviewActivity.this.overridePendingTransition(0, com.luck.picture.lib.R.anim.a3);
                    }
                });
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if(MyPictureExternalPreviewActivity.this.rxPermissions == null) {
                            MyPictureExternalPreviewActivity.this.rxPermissions = new RxPermissions(MyPictureExternalPreviewActivity.this);
                        }

                        MyPictureExternalPreviewActivity.this.rxPermissions.request(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).subscribe(new Observer() {
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(@NonNull Object o) {

                            }

                            public void onNext(Boolean aBoolean) {
                                if(aBoolean.booleanValue()) {
                                    MyPictureExternalPreviewActivity.this.showDownLoadDialog(path);
                                } else {
                                    MyPictureExternalPreviewActivity.this.showToast(MyPictureExternalPreviewActivity.this.getString(com.luck.picture.lib.R.string.picture_jurisdiction));
                                }

                            }

                            public void onError(Throwable e) {
                            }

                            public void onComplete() {
                            }
                        });
                        return true;
                    }
                });
            }

            container.addView(contentView, 0);
            return contentView;
        }
    }
}
