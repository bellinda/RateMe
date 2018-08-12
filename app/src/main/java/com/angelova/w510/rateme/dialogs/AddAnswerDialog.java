package com.angelova.w510.rateme.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.angelova.w510.rateme.MenuActivity;
import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.models.Answer;
import com.angelova.w510.rateme.models.Request;

/**
 * Created by W510 on 12.8.2018 г..
 */

public class AddAnswerDialog extends Dialog {

    private DialogClickListener mListener;
    private Activity activity;
    private Request request;
    private String currentUser;
    private EditText mAnswerInput;
    private TextView mRequestData;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    public interface DialogClickListener {
        void onSave(Answer answer);
    }

    public AddAnswerDialog(Activity activity, Request request, String currentUser, DialogClickListener listener) {
        super(activity, R.style.Theme_AppCompat_Light_Dialog_Alert);

        this.activity = activity;
        this.mListener = listener;
        this.request = request;
        this.currentUser = currentUser;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_answer);

        mAnswerInput = (EditText) findViewById(R.id.answer_input);
        mRequestData = (TextView) findViewById(R.id.request_data);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mRequestData.setText(String.format("To %s\n%s € ➡ %s €", request.getAuthor(), request.getCurrentHourRate(), request.getDesiredHourRate()));

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerInput.getText() == null || TextUtils.isEmpty(mAnswerInput.getText())) {
                    ((MenuActivity) activity).showAlertDialogNow(activity.getString(R.string.dialog_add_answer_error_title), activity.getString(R.string.dialog_add_answer_nothing));
                } else {
                    Answer answer = new Answer();
                    answer.setAuthor(currentUser);
                    answer.setContent(mAnswerInput.getText().toString());
                    mListener.onSave(answer);
                    dismiss();
                }
            }
        });
    }
}
