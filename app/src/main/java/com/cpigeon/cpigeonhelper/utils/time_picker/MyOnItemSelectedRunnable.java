package com.cpigeon.cpigeonhelper.utils.time_picker;

final class MyOnItemSelectedRunnable implements Runnable {
    final MyWheelView loopView;

    MyOnItemSelectedRunnable(MyWheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
