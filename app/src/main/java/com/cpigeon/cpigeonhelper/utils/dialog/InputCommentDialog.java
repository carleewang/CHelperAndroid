package com.cpigeon.cpigeonhelper.utils.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;




/**
 * Created by Zhu TingYu on 2018/1/8.
 */

public class InputCommentDialog extends DialogFragment {

    TextView btn;
    public EditText content;

    private OnPushClickListener listener;

    public Dialog dialog;

    boolean keyboardIsOpen = false;

    String hint;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null){
            hint = getArguments().getString(IntentBuilder.KEY_DATA);
        }
        KeyboardVisibilityEvent.setEventListener(
                getActivity(), b -> {
                    keyboardIsOpen = b;
                });
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_fragment_news_comment_layout);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView(dialog);
        dialog.setOnShowListener(dialog1 -> {
            if(!keyboardIsOpen){
               toggleInput(getActivity());
            }
        });
        return dialog;
    }

    private void initView(Dialog dialog) {
        btn = (TextView) dialog.findViewById(R.id.text_btn);
        content = (EditText) dialog.findViewById(R.id.content);
        content.setHint(hint);
        btn.setOnClickListener(v -> {
            listener.click(content);
        });
    }

    public void closeDialog() {
        //CommonUitls.hideSoftInput(getActivity(), content);
        dismiss();
    }

    public void setHint(String hint){
        Bundle bundle = new Bundle();
        bundle.putString(IntentBuilder.KEY_DATA,hint);
        setArguments(bundle);
    }

    public interface OnPushClickListener {
        void click(EditText editText);
    }

    public void setPushClickListener(OnPushClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(keyboardIsOpen){
          toggleInput(getActivity());
        }
        super.onDismiss(dialog);
    }

    public  void toggleInput(Context context){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
