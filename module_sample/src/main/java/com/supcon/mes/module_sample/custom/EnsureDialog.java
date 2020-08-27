package com.supcon.mes.module_sample.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.mes.module_sample.R;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/27
 * Email:wanghaidong1@supcon.com
 */
public class EnsureDialog extends Dialog {
    TextView btn_cancel;
    TextView btn_ensure;
    public EnsureDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recordresult);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_ensure=findViewById(R.id.btn_ensure);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RxView.clicks(btn_cancel)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    dismiss();
                });

        RxView.clicks(btn_ensure)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (onPositiveClickListener!=null){
                        onPositiveClickListener.onPositiveClick();
                    }
                });
    }
    private OnPositiveClickListener onPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }


    public interface OnPositiveClickListener {
        void onPositiveClick();
    }


}
