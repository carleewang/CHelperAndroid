package com.cpigeon.cpigeonhelper.utils.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/7/2.
 */

public class MyPercentFormatter  implements ValueFormatter, AxisValueFormatter {

    protected FormattedStringCache.Generic<Integer, Float> mFormattedStringCache;
    protected FormattedStringCache.PrimFloat mFormattedStringCacheAxis;

    public MyPercentFormatter() {
        mFormattedStringCache = new FormattedStringCache.Generic<>(new DecimalFormat("###,###,##0.0"));
        mFormattedStringCacheAxis = new FormattedStringCache.PrimFloat(new DecimalFormat("###,###,##0.0"));
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public MyPercentFormatter(DecimalFormat format) {
        mFormattedStringCache = new FormattedStringCache.Generic<>(format);
        mFormattedStringCacheAxis = new FormattedStringCache.PrimFloat(format);
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "";
    }

    // AxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // TODO: Find a better way to do this.  Float isn't the best key...
        return "";
    }

    @Override
    public int getDecimalDigits() {
        return 1;
    }
}
