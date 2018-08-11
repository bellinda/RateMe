package com.angelova.w510.rateme.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.adapters.OwnRequestsAnswersAdapter;
import com.angelova.w510.rateme.models.Answer;

import java.util.List;

/**
 * Created by W510 on 12.8.2018 г..
 */

public class AnswersDialog extends Dialog {

    private Context activity;
    private List<Answer> answers;
    private int recipientsCount;

    private TextView dialogTitle;
    private RecyclerView recyclerView;
    private OwnRequestsAnswersAdapter adapter;
    private TextView okBtn;

    public AnswersDialog(Activity activity, List<Answer> answers, int recipientsCount) {
        super(activity, R.style.Theme_AppCompat_Light_Dialog_Alert);

        this.activity = activity;
        this.answers = answers;
        this.recipientsCount = recipientsCount;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_answers);

        dialogTitle = (TextView) findViewById(R.id.title_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        okBtn = (TextView) findViewById(R.id.ok_button);

        dialogTitle.setText(String.format("%s/%s Answers", answers.size(), recipientsCount));

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        adapter = new OwnRequestsAnswersAdapter(answers, activity);
        recyclerView.setAdapter(adapter);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
