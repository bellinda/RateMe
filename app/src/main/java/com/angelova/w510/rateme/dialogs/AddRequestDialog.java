package com.angelova.w510.rateme.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.models.Answer;
import com.angelova.w510.rateme.models.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class AddRequestDialog extends Dialog {

    private DialogClickListener mListener;
    private Activity activity;
    private String[] possibleRecipients;
    private EditText mCurrentRate;
    private EditText mDesiredRate;
    private MultiAutoCompleteTextView mRecipients;
    private EditText mComment;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    public interface DialogClickListener {
        void onSave(Request request);
    }

    public AddRequestDialog(Activity activity, String[] possibleRecipients, DialogClickListener listener) {
        super(activity, R.style.Theme_AppCompat_Light_Dialog_Alert);

        this.activity = activity;
        this.mListener = listener;
        this.possibleRecipients = possibleRecipients;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_request);

        mCurrentRate = (EditText) findViewById(R.id.current_rate);
        mDesiredRate = (EditText) findViewById(R.id.desired_rate);
        mRecipients = (MultiAutoCompleteTextView) findViewById(R.id.recipients);
        mComment = (EditText) findViewById(R.id.comment);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, possibleRecipients);

        // Connect the data source with AutoCompleteTextView through adapter.
        mRecipients.setAdapter(arrayAdapter);

        // Must set tokenizer for MultiAutoCompleteTextView object, otherwise it will not take effect.
        mRecipients.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDesiredRate.getText() == null || TextUtils.isEmpty(mDesiredRate.getText())) {
                    showAlertDialogNow(activity.getString(R.string.dialog_add_request_no_desired_rate), activity.getString(R.string.dialog_add_request_error_title));
                } else if (mCurrentRate.getText() == null || TextUtils.isEmpty(mCurrentRate.getText())) {
                    showAlertDialogNow(activity.getString(R.string.dialog_add_request_no_current_rate), activity.getString(R.string.dialog_add_request_error_title));
                } else if (mRecipients.getText() == null || TextUtils.isEmpty(mRecipients.getText())) {
                    showAlertDialogNow(activity.getString(R.string.dialog_add_request_no_recipients), activity.getString(R.string.dialog_add_request_error_title));
                } else {

                    Request request = new Request();
                    request.setCurrentHourRate(Double.parseDouble(mCurrentRate.getText().toString()));
                    request.setDesiredHourRate(Double.parseDouble(mDesiredRate.getText().toString()));
                    String recipientsString = mRecipients.getText().toString();
                    recipientsString = recipientsString.trim();
                    if (recipientsString.endsWith(",")) {
                        recipientsString = recipientsString.substring(0, recipientsString.length() - 1);
                    }
                    List<String> recipients = new ArrayList<String>(Arrays.asList(recipientsString.split(",")));
                    request.setRecipients(recipients);
                    if (mComment.getText() != null && !TextUtils.isEmpty(mComment.getText())) {
                        request.setComment(mComment.getText().toString());
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    Calendar calendar = Calendar.getInstance();
                    request.setDate(sdf.format(calendar.getTime()));
                    request.setAnswers(new ArrayList<Answer>());
                    mListener.onSave(request);
                    dismiss();
                }
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(activity, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {

            }
        });
        warning.show();
    }
}
