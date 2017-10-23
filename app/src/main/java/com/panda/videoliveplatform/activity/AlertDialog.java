package com.panda.videoliveplatform.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.panda.videoliveplatform.R;

public class AlertDialog extends Dialog {
    private int mClickType = R.id.button_cancel;
    private String mContentText;
    private Context mContext;
    private DEFAULT_BTN mDefaultButton;
    private String mNoText;
    private String mYesText;

    enum DEFAULT_BTN {
        DEFAULT_NONE,
        DEFAULT_YES,
        DEFAULT_NO
    }

    public AlertDialog(Context context, String contentText, String yesText, String noText, DEFAULT_BTN default_button) {
        super(context);
        this.mContext = context;
        this.mContentText = contentText;
        this.mYesText = yesText;
        this.mNoText = noText;
        this.mDefaultButton = default_button;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.alert_dialog);
        InitDialog();
    }

    public void InitDialog() {
        Resources resources = this.mContext.getResources();
        Button noBtn = (Button) findViewById(R.id.button_cancel);
        noBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.this.mClickType = R.id.button_cancel;
                AlertDialog.this.dismiss();
            }
        });
        noBtn.setText(this.mNoText);
        if (this.mDefaultButton == DEFAULT_BTN.DEFAULT_NO) {
            noBtn.setTextColor(resources.getColor(R.color.alert_dialog_hightlight));
        } else {
            noBtn.setTextColor(resources.getColor(R.color.alert_dialog_darklight));
        }
        Button yesBtn = (Button) findViewById(R.id.button_continue);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.this.mClickType = R.id.button_continue;
                AlertDialog.this.dismiss();
            }
        });
        yesBtn.setText(this.mYesText);
        if (this.mDefaultButton == DEFAULT_BTN.DEFAULT_YES) {
            yesBtn.setTextColor(resources.getColor(R.color.alert_dialog_hightlight));
        } else {
            yesBtn.setTextColor(resources.getColor(R.color.alert_dialog_darklight));
        }
        ((TextView) findViewById(R.id.content_text)).setText(this.mContentText);
    }

    public int GetClickType() {
        return this.mClickType;
    }
}
