package com.angelova.w510.rateme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.models.Answer;

import java.util.List;

/**
 * Created by W510 on 12.8.2018 г..
 */

public class OwnRequestsAnswersAdapter extends RecyclerView.Adapter<OwnRequestsAnswersAdapter.ViewHolder> {

    private List<Answer> answers;
    private Context context;

    public OwnRequestsAnswersAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.own_request_answer_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Answer answer = answers.get(position);
        holder.fromText.setText(answer.getAuthor());
        holder.content.setText(answer.getContent());
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fromText;
        private TextView content;

        public ViewHolder(View view) {
            super(view);
            fromText = (TextView) view.findViewById(R.id.from_text);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}
