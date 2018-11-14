package com.cpigeon.cpigeonhelper.ui.materialcalendarview;


import android.content.Context;
import android.os.Build;

import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.ui.materialcalendarview.spans.ImageSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private HashSet<CalendarDay> dates;

    public EventDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

    public EventDecorator(CalendarDay dates) {
        this.dates = new HashSet<>();
        this.dates.add(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        Context context = MyApplication.getInstance().getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.addSpan(new ImageSpan(context, R.drawable.ic_hook));
        }
    }
}
