package com.cpigeon.cpigeonhelper.message.base;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.utils.DrawableUtils;

/**
 * Created by Zhu TingYu on 2017/11/17.
 */


public class BaseViewHolder extends com.chad.library.adapter.base.BaseViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * an exception if the view doesn't exist.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T result = (T) itemView.findViewById(id);
        if (result == null) {
            return null;
        }
        return result;
    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return getView(resId);
    }

    public static View inflater(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public static View inflater(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public Drawable getDrawable(int res) {

        Drawable drawable;
        try {
            drawable = AppCompatResources.getDrawable(itemView.getContext(), res);
            DrawableUtils.fixDrawable(drawable);
        } catch (Resources.NotFoundException e) {
            drawable = itemView.getContext().getResources().getDrawable(res);
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public int getColors(@ColorRes int resId) {
        return itemView.getContext().getResources().getColor(resId);
    }

    public String getString(@StringRes int resId, String s) {
        return itemView.getContext().getResources().getString(resId) + s;
    }

    public String getString(@StringRes int resId) {
        return itemView.getContext().getResources().getString(resId);
    }

    public void setViewDrawableRight(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null
                , getDrawable(resId), null);

    }

    public void setTextView(TextView textView, CharSequence... text) {
        if (textView == null) return;
        CharSequence t = getArrayString(text);
        if (TextUtils.isEmpty(t)) textView.setText("");
        else textView.setText(t);
        textView.setVisibility(View.VISIBLE);
    }

    public void setTextView(@IdRes int resId, CharSequence... text) {
        TextView textView = getView(resId);
        setTextView(textView, text);
    }

    private CharSequence getArrayString(CharSequence... text) {
        String s = "";
        StringBuilder str = new StringBuilder();
        if (text == null || text.length == 0) {
            return str;
        }
        boolean isImages = false;
        for (CharSequence img : text) {
            str.append(img);
            str.append(s);
            isImages = true;
        }
        if (isImages && s.length() > 0) {
            return str.substring(0, str.length() - s.length());
        }
        return str;
    }

//    public void setIconView(@IdRes int resId, String url) {
//        CustomDraweeView icon = getView(resId);
//        if (icon != null) {
//            BaseImageLoadUtil.Builder().load(url)
//                    .build().imageOptions(R.color.color_999999)
//                    .displayImage(icon);
//        }
//    }

    public void setViewSize(@IdRes int resId, int w, int h) {
        View view = getView(resId);
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = w;
            lp.height = h;
            view.requestLayout();
        }
    }

    public static void setViewSize(View parent, @IdRes int resId, int w, int h) {
        View view = parent.findViewById(resId);
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.width = w;
            lp.height = h;
            view.requestLayout();
        }
    }

//    public void setIconView(@IdRes int resId, Uri url) {
//        CustomDraweeView icon = getView(resId);
//        if (icon != null) {
//            ImageLoadUtil.Builder().load(url)
//                    .build().imageOptions(R.color.color_divider)
//                    .displayImage(icon);
//        }
//    }

    public void setIconView(@IdRes int resId, @DrawableRes int url) {
        AppCompatImageView icon = getView(resId);
        if (icon != null) {
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.mipmap.head_image_default)
                    .error(R.mipmap.head_image_default)
                    .into(icon);
        }
    }

    public void setIconView(@IdRes int resId, String url) {
        AppCompatImageView icon = getView(resId);
        if (icon != null) {
            Glide.with(itemView.getContext())
                    .load(url)
                    .placeholder(R.mipmap.head_image_default)
                    .error(R.mipmap.head_image_default)
                    .into(icon);
        }
    }

    public void setHeadImageView(@IdRes int resId, String url) {
        ImageView icon = getView(resId);
        if (icon != null) {
            Glide.with(itemView.getContext())
                    .load(url)
                    .error(R.mipmap.head)
                    .into(icon);
        }
    }

    public void setViewVisible(@IdRes int resId, int visible) {
        View view = getView(resId);
        if (view != null) {
            view.setVisibility(visible);
        }
    }

}
