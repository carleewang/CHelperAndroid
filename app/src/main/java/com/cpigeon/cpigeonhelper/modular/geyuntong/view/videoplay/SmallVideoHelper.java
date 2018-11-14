package com.cpigeon.cpigeonhelper.modular.geyuntong.view.videoplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.cpigeon.cpigeonhelper.modular.geyuntong.view.fragment.PigeonMonitorFragment;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.transitionseverywhere.TransitionManager;

import java.io.File;
import java.util.Map;

import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getActionBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.getStatusBarHeight;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideNavKey;
import static com.shuyu.gsyvideoplayer.utils.CommonUtil.showNavKey;

/**
 * 列表模式的小屏和全屏工具类
 * Created by guoshuyu on 2018/1/15.
 */
public class SmallVideoHelper {

    /**
     * 播放的标志
     */
    private String TAG = "NULL";
    /**
     * 播放器
     */
    private StandardGSYVideoPlayer gsyVideoPlayer;
    /**
     * 全屏承载布局
     */
    private ViewGroup fullViewContainer;
    /**
     * 全屏承载布局
     */
    private ViewGroup windowViewContainer;
    /**
     * 记录列表中item的父布局
     */
    private ViewGroup listParent;
    /**
     * 布局
     */
    private ViewGroup.LayoutParams listParams;
    /**
     * 选择工具类
     */
    private OrientationUtils orientationUtils;
    /**
     * 播放配置
     */
    private GSYSmallVideoHelperBuilder gsyVideoOptionBuilder;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 播放的位置
     */
    private int playPosition = -1;
    /**
     * 可视保存
     */
    private int systemUiVisibility;
    /**
     * 当前是否全屏
     */
    private boolean isFull;
    /**
     * 当前是否小屏
     */
    private boolean isSmall;
    /**
     * 当前item框的屏幕位置
     */
    private int[] listItemRect;
    /**
     * 当前item的大小
     */
    private int[] listItemSize;
    /**
     * handler
     */
    private Handler handler = new Handler();


    public SmallVideoHelper(Context context) {
        this(context, new StandardGSYVideoPlayer(context));
    }

    public SmallVideoHelper(Context context, StandardGSYVideoPlayer player) {
        try {
            gsyVideoPlayer = player;
            this.context = context;
            this.windowViewContainer = (ViewGroup) (CommonUtil.scanForActivity(context)).findViewById(Window.ID_ANDROID_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理全屏逻辑
     */
    private void resolveToFull() {
        try {
            systemUiVisibility = ((Activity) context).getWindow().getDecorView().getSystemUiVisibility();
            CommonUtil.hideSupportActionBar(context, gsyVideoOptionBuilder.isHideActionBar(), gsyVideoOptionBuilder.isHideStatusBar());
            if (gsyVideoOptionBuilder.isHideKey()) {
                hideNavKey(context);
            }
            isFull = true;
            ViewGroup viewGroup = (ViewGroup) gsyVideoPlayer.getParent();
            listParams = gsyVideoPlayer.getLayoutParams();
            if (viewGroup != null) {
                listParent = viewGroup;
                viewGroup.removeView(gsyVideoPlayer);
            }
            gsyVideoPlayer.setIfCurrentIsFullscreen(true);
            gsyVideoPlayer.getFullscreenButton().setImageResource(gsyVideoPlayer.getShrinkImageRes());
            gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
            //设置旋转
            orientationUtils = new OrientationUtils((Activity) context, gsyVideoPlayer);
            orientationUtils.setEnable(gsyVideoOptionBuilder.isRotateViewAuto());
            gsyVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolveMaterialToNormal(gsyVideoPlayer);
                }
            });
            if (gsyVideoOptionBuilder.isShowFullAnimation()) {
                if (fullViewContainer instanceof FrameLayout) {
                    //目前只做了frameLoayout的判断
                    resolveMaterialAnimation();
                } else {
                    resolveFullAdd();
                }

            } else {
                resolveFullAdd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加到全屏父布局里
     */
    private void resolveFullAdd() {
        try {
            if (gsyVideoOptionBuilder.isShowFullAnimation()) {
                if (fullViewContainer != null) {
                    fullViewContainer.setBackgroundColor(Color.BLACK);
                }
            }
            resolveChangeFirstLogic(0);
            if (fullViewContainer != null) {
                fullViewContainer.addView(gsyVideoPlayer);
            } else {
                windowViewContainer.addView(gsyVideoPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果是5.0的动画开始位置
     */
    private void resolveMaterialAnimation() {
        try {
            listItemRect = new int[2];
            listItemSize = new int[2];
            saveLocationStatus(context, gsyVideoOptionBuilder.isHideActionBar(), gsyVideoOptionBuilder.isHideStatusBar());
            FrameLayout.LayoutParams lpParent = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setBackgroundColor(Color.BLACK);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(listItemSize[0], listItemSize[1]);
            lp.setMargins(listItemRect[0], listItemRect[1], 0, 0);
            frameLayout.addView(gsyVideoPlayer, lp);
            if (fullViewContainer != null) {
                fullViewContainer.addView(frameLayout, lpParent);
            } else {
                windowViewContainer.addView(frameLayout, lpParent);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //开始动画
                    if (fullViewContainer != null) {
                        TransitionManager.beginDelayedTransition(fullViewContainer);
                    } else {
                        TransitionManager.beginDelayedTransition(windowViewContainer);
                    }

                    resolveMaterialFullVideoShow(gsyVideoPlayer);
                    //                resolveChangeFirstLogic(600);
                }
            }, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果是5.0的，要从原位置过度到全屏位置
     */
    private void resolveMaterialFullVideoShow(GSYBaseVideoPlayer gsyVideoPlayer) {
        try {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) gsyVideoPlayer.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            gsyVideoPlayer.setLayoutParams(lp);
            gsyVideoPlayer.setIfCurrentIsFullscreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理正常逻辑
     */
    private void resolveToNormal() {
        try {
            int delay = orientationUtils.backToProtVideo();
            if (!gsyVideoOptionBuilder.isShowFullAnimation()) {
                delay = 0;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isFull = false;
                    removeWindowContainer();
                    if (fullViewContainer != null) {
                        fullViewContainer.removeAllViews();
                    }
                    if (gsyVideoPlayer.getParent() != null) {
                        ((ViewGroup) gsyVideoPlayer.getParent()).removeView(gsyVideoPlayer);
                    }
                    orientationUtils.setEnable(false);
                    gsyVideoPlayer.setIfCurrentIsFullscreen(false);
                    if (fullViewContainer != null) {
                        fullViewContainer.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (listParent != null && gsyVideoPlayer != null && listParams != null) {
                        listParent.addView(gsyVideoPlayer, listParams);
                    }

                    gsyVideoPlayer.getFullscreenButton().setImageResource(gsyVideoPlayer.getEnlargeImageRes());
                    gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
                    gsyVideoPlayer.setIfCurrentIsFullscreen(false);
                    if (gsyVideoOptionBuilder.getVideoAllCallBack() != null) {
                        Debuger.printfLog("onQuitFullscreen");
                        gsyVideoOptionBuilder.getVideoAllCallBack().onQuitFullscreen(gsyVideoOptionBuilder.getUrl(), gsyVideoOptionBuilder.getVideoTitle(), gsyVideoPlayer);
                    }
                    if (gsyVideoOptionBuilder.isHideKey()) {
                        showNavKey(context, systemUiVisibility);
                    }
                    CommonUtil.showSupportActionBar(context, gsyVideoOptionBuilder.isHideActionBar(), gsyVideoOptionBuilder.isHideStatusBar());
                }
            }, delay);
        } catch (Exception e) {

        }
    }


    /**
     * 动画回到正常效果
     */
    private void resolveMaterialToNormal(final GSYVideoPlayer gsyVideoPlayer) {
        try {
            if (gsyVideoOptionBuilder.isShowFullAnimation() && fullViewContainer instanceof FrameLayout) {
                int delay = orientationUtils.backToProtVideo();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TransitionManager.beginDelayedTransition(fullViewContainer);
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) gsyVideoPlayer.getLayoutParams();
                            lp.setMargins(listItemRect[0], listItemRect[1], 0, 0);
                            lp.width = listItemSize[0];
                            lp.height = listItemSize[1];
                            //注意配置回来，不然动画效果会不对
                            lp.gravity = Gravity.NO_GRAVITY;
                            gsyVideoPlayer.setLayoutParams(lp);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    resolveToNormal();
                                }
                            }, 400);
                        } catch (Exception e) {

                        }
                    }
                }, delay);
            } else {
                resolveToNormal();
            }
        } catch (Exception e) {

        }
    }


    /**
     * 是否全屏一开始马上自动横屏
     */
    private void resolveChangeFirstLogic(int time) {

        try {
            if (gsyVideoOptionBuilder.isLockLand()) {
                if (time > 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (orientationUtils.getIsLand() != 1) {
                                    if (fullViewContainer != null) {
                                        fullViewContainer.setBackgroundColor(Color.BLACK);
                                    }
                                    orientationUtils.resolveByClick();
                                }
                            } catch (Exception e) {

                            }

                        }
                    }, time);
                } else {
                    if (orientationUtils.getIsLand() != 1) {
                        if (fullViewContainer != null) {
                            fullViewContainer.setBackgroundColor(Color.BLACK);
                        }
                        orientationUtils.resolveByClick();
                    }
                }
            }
            gsyVideoPlayer.setIfCurrentIsFullscreen(true);
            if (gsyVideoOptionBuilder.getVideoAllCallBack() != null) {
                Debuger.printfLog("onEnterFullscreen");
                gsyVideoOptionBuilder.getVideoAllCallBack().onEnterFullscreen(gsyVideoOptionBuilder.getUrl(), gsyVideoOptionBuilder.getVideoTitle(), gsyVideoPlayer);
            }
        } catch (Exception e) {

        }

    }

    /**
     * 保存大小和状态
     */
    private void saveLocationStatus(Context context, boolean statusBar, boolean actionBar) {

        try {
            listParent.getLocationOnScreen(listItemRect);
            int statusBarH = getStatusBarHeight(context);
            int actionBerH = getActionBarHeight((Activity) context);
            if (statusBar) {
                listItemRect[1] = listItemRect[1] - statusBarH;
            }
            if (actionBar) {
                listItemRect[1] = listItemRect[1] - actionBerH;
            }
            listItemSize[0] = listParent.getWidth();
            listItemSize[1] = listParent.getHeight();
        } catch (Exception e) {

        }
    }


    /**
     * 是否当前播放
     */
    private boolean isPlayView(int position, String tag) {
        return playPosition == position && TAG.equals(tag);
    }

    private boolean isCurrentViewPlaying(int position, String tag) {
        return isPlayView(position, tag);
    }

    private boolean removeWindowContainer() {
        try {
            if (windowViewContainer != null && windowViewContainer.indexOfChild(gsyVideoPlayer) != -1) {
                windowViewContainer.removeView(gsyVideoPlayer);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 动态添加视频播放
     *
     * @param position  位置
     * @param imgView   封面
     * @param tag       TAG类型
     * @param container player的容器
     * @param playBtn   播放按键
     */
    public void addVideoPlayer(final int position, View imgView, String tag,
                               ViewGroup container, View playBtn) {

        try {
            container.removeAllViews();
            if (isCurrentViewPlaying(position, tag)) {
                if (!isFull) {
                    ViewGroup viewGroup = (ViewGroup) gsyVideoPlayer.getParent();
                    if (viewGroup != null)
                        viewGroup.removeAllViews();
                    container.addView(gsyVideoPlayer);
                    playBtn.setVisibility(View.INVISIBLE);
                }
            } else {
                playBtn.setVisibility(View.VISIBLE);
                container.removeAllViews();   //增加封面
                container.addView(imgView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置列表播放中的位置和TAG,防止错位，回复播放位置
     *
     * @param playPosition 列表中的播放位置
     * @param tag          播放的是哪个列表的tag
     */
    public void setPlayPositionAndTag(int playPosition, String tag) {
        try {
            this.playPosition = playPosition;
            this.TAG = tag;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void startPlay() {

        try {
            if (isSmall()) {
                smallVideoToNormal();
            }

            gsyVideoPlayer.release();


            if (gsyVideoOptionBuilder == null) {
                throw new NullPointerException("gsyVideoOptionBuilder can't be null");
            }

            gsyVideoOptionBuilder.build(gsyVideoPlayer);

            //增加title
            if (gsyVideoPlayer.getTitleTextView() != null) {
                gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            }

            //设置返回键
            if (gsyVideoPlayer.getBackButton() != null) {
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            }

            //设置全屏按键功能
            if (gsyVideoPlayer.getFullscreenButton() != null) {
                gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d("isFull", "startPlay: 1   " + isFull);
                            PigeonMonitorFragment.ff_tag_z.setVisibility(View.GONE);
                            PigeonMonitorFragment.mViewPager.setScanScroll(false);
                            doFullBtnLogic();
                        } catch (Exception e) {

                        }
                    }
                });
            }
            gsyVideoPlayer.startPlayLogic();

            gsyVideoPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gsyVideoPlayer.getFullscreenButton().performClick();
                            gsyVideoPlayer.startPlayLogic();
                        }
                    }, 300);
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gsyVideoPlayer.getFullscreenButton().performClick();
                }
            }, 300);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 全屏按键逻辑
     */
    public void doFullBtnLogic() {
        try {
            if (!isFull) {
                resolveToFull();
            } else {
                resolveMaterialToNormal(gsyVideoPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理返回正常逻辑
     */
    public boolean backFromFull() {
        boolean isFull = false;
        try {
            if (fullViewContainer != null && fullViewContainer.getChildCount() > 0) {
                isFull = true;
                resolveMaterialToNormal(gsyVideoPlayer);
            } else if (windowViewContainer != null && windowViewContainer.indexOfChild(gsyVideoPlayer) != -1) {
                isFull = true;
                resolveMaterialToNormal(gsyVideoPlayer);
            }
            return isFull;
        } catch (Exception e) {

        }
        return isFull;
    }

    /**
     * 释放持有的视频
     */
    public void releaseVideoPlayer() {
        try {
            removeWindowContainer();
            ViewGroup viewGroup = (ViewGroup) gsyVideoPlayer.getParent();
            if (viewGroup != null)
                viewGroup.removeAllViews();
            playPosition = -1;
            TAG = "NULL";
            if (orientationUtils != null)
                orientationUtils.releaseListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示小屏幕效果
     *
     * @param size      小视频的大小
     * @param actionBar 是否有actionBar
     * @param statusBar 是否有状态栏
     */
    public void showSmallVideo(Point size, final boolean actionBar, final boolean statusBar) {
        try {
            if (gsyVideoPlayer.getCurrentState() == GSYVideoPlayer.CURRENT_STATE_PLAYING) {
                gsyVideoPlayer.showSmallVideo(size, actionBar, statusBar);
                isSmall = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 恢复小屏幕效果
     */
    public void smallVideoToNormal() {
        try {
            isSmall = false;
            gsyVideoPlayer.hideSmallVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置全屏显示的viewGroup
     * 如果不设置即使用默认的 windowViewContainer
     *
     * @param fullViewContainer viewGroup
     */
    public void setFullViewContainer(ViewGroup fullViewContainer) {
        this.fullViewContainer = fullViewContainer;
    }

    /**
     * 是否全屏
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * 设置配置
     */
    public void setGsyVideoOptionBuilder(GSYSmallVideoHelperBuilder gsyVideoOptionBuilder) {
        this.gsyVideoOptionBuilder = gsyVideoOptionBuilder;
    }

    public GSYVideoOptionBuilder getGsyVideoOptionBuilder() {
        return gsyVideoOptionBuilder;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public String getPlayTAG() {
        return TAG;
    }

    public boolean isSmall() {
        return isSmall;
    }

    /**
     * 获取播放器,直接拿播放器，根据需要自定义配置
     */
    public StandardGSYVideoPlayer getGsyVideoPlayer() {
        return gsyVideoPlayer;
    }

    /**
     * 配置
     */
    public static class GSYSmallVideoHelperBuilder extends GSYVideoOptionBuilder {

        protected boolean mHideActionBar;

        protected boolean mHideStatusBar;

        public boolean isHideActionBar() {
            return mHideActionBar;
        }

        public GSYSmallVideoHelperBuilder setHideActionBar(boolean hideActionBar) {
            this.mHideActionBar = hideActionBar;
            return this;
        }

        public boolean isHideStatusBar() {
            return mHideStatusBar;
        }

        public GSYSmallVideoHelperBuilder setHideStatusBar(boolean hideStatusBar) {
            this.mHideStatusBar = hideStatusBar;
            return this;
        }

        public int getShrinkImageRes() {
            return mShrinkImageRes;
        }

        public int getEnlargeImageRes() {
            return mEnlargeImageRes;
        }

        public int getPlayPosition() {
            return mPlayPosition;
        }

        public int getDialogProgressHighLightColor() {
            return mDialogProgressHighLightColor;
        }

        public int getDialogProgressNormalColor() {
            return mDialogProgressNormalColor;
        }

        public int getDismissControlTime() {
            return mDismissControlTime;
        }

        public long getSeekOnStart() {
            return mSeekOnStart;
        }

        public float getSeekRatio() {
            return mSeekRatio;
        }

        public float getSpeed() {
            return mSpeed;
        }

        public boolean isHideKey() {
            return mHideKey;
        }

        public boolean isShowFullAnimation() {
            return mShowFullAnimation;
        }

        public boolean isNeedShowWifiTip() {
            return mNeedShowWifiTip;
        }

        public boolean isRotateViewAuto() {
            return mRotateViewAuto;
        }

        public boolean isLockLand() {
            return mLockLand;
        }

        public boolean isLooping() {
            return mLooping;
        }

        public boolean isIsTouchWiget() {
            return mIsTouchWiget;
        }

        public boolean isIsTouchWigetFull() {
            return mIsTouchWigetFull;
        }

        public boolean isShowPauseCover() {
            return mShowPauseCover;
        }

        public boolean isRotateWithSystem() {
            return mRotateWithSystem;
        }

        public boolean isCacheWithPlay() {
            return mCacheWithPlay;
        }

        public boolean isNeedLockFull() {
            return mNeedLockFull;
        }

        public boolean isThumbPlay() {
            return mThumbPlay;
        }

        public boolean isSounchTouch() {
            return mSounchTouch;
        }

        public boolean isSetUpLazy() {
            return mSetUpLazy;
        }

        public String getPlayTag() {
            return mPlayTag;
        }

        public String getUrl() {
            return mUrl;
        }

        public String getVideoTitle() {
            return mVideoTitle;
        }

        public File getCachePath() {
            return mCachePath;
        }

        public Map<String, String> getMapHeadData() {
            return mMapHeadData;
        }

        public VideoAllCallBack getVideoAllCallBack() {
            return mVideoAllCallBack;
        }

        public StandardVideoAllCallBack getStandardVideoAllCallBack() {
            return mStandardVideoAllCallBack;
        }

        public LockClickListener getLockClickListener() {
            return mLockClickListener;
        }

        public View getThumbImageView() {
            return mThumbImageView;
        }

        public Drawable getBottomProgressDrawable() {
            return mBottomProgressDrawable;
        }

        public Drawable getBottomShowProgressDrawable() {
            return mBottomShowProgressDrawable;
        }

        public Drawable getBottomShowProgressThumbDrawable() {
            return mBottomShowProgressThumbDrawable;
        }

        public Drawable getVolumeProgressDrawable() {
            return mVolumeProgressDrawable;
        }

        public Drawable getDialogProgressBarDrawable() {
            return mDialogProgressBarDrawable;
        }

        public GSYVideoGLView.ShaderInterface getEffectFilter() {
            return mEffectFilter;
        }

        public GSYVideoProgressListener getGSYVideoProgressListener() {
            return mGSYVideoProgressListener;
        }
    }


}
