package com.cpigeon.cpigeonhelper.utils.time_picker;

import android.view.MotionEvent;

final class MyLoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final MyWheelView loopView;

    MyLoopViewGestureListener(MyWheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
