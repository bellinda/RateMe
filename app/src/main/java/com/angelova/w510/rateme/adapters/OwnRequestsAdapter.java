package com.angelova.w510.rateme.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.models.Request;

import java.util.List;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class OwnRequestsAdapter extends RecyclerView.Adapter<OwnRequestsAdapter.ViewHolder> {

    private List<Request> requests;
    private Context context;

    public OwnRequestsAdapter(List<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.own_request_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Request request = requests.get(position);
        String rate = String.format("%s € ➡ %s €", request.getCurrentHourRate(), request.getDesiredHourRate());
        holder.rateView.setText(rate);
        holder.dateView.setText(request.getDate());
        holder.answersCountView.setText(String.format("%s answer(s) received", request.getAnswers().size()));
        if (request.getAnswers().size() == 0) {
           holder.colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.color_indicator_no));
        } else if (request.getAnswers().size() < request.getRecipients().size()) {
            holder.colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.color_indicator_not_all));
        } else {
            holder.colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.color_indicator_all));
        }
        holder.viewAnswersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rateView;
        private TextView dateView;
        private TextView answersCountView;
        private TextView viewAnswersBtn;
        private View colorIndicator;

        public ViewHolder(View view) {
            super(view);
            rateView = (TextView) view.findViewById(R.id.rate_view);
            dateView = (TextView) view.findViewById(R.id.date_view);
            answersCountView = (TextView) view.findViewById(R.id.answers_count_view);
            viewAnswersBtn = (TextView) view.findViewById(R.id.view_answers_btn);
            colorIndicator = view.findViewById(R.id.color_indicator);
        }
    }
}
