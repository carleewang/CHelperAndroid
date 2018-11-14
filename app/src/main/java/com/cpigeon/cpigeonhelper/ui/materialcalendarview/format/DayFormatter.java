package com.cpigeon.cpigeonhelper.ui.materialcalendarview.format;


import android.support.annotation.NonNull;

import com.cpigeon.cpigeonhelper.ui.materialcalendarview.CalendarDay;

public interface DayFormatter {

    /**
     * Format a given day into a string
     *
     * @param day the day
     * @return a label for the day
     */
    @NonNull
    String format(@NonNull CalendarDay day);

    /**
     */
    public static final DayFormatter DEFAULT = new DateFormatDayFormatter();
}
