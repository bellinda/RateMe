package com.angelova.w510.rateme.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.rateme.MenuActivity;
import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.models.Answer;
import com.angelova.w510.rateme.models.Request;

import java.util.List;

/**
 * Created by W510 on 12.8.2018 г..
 */

public class ReceivedRequestsAdapter extends RecyclerView.Adapter<ReceivedRequestsAdapter.ViewHolder> {

    private List<Request> requests;
    private Context context;
    private String currentUser;

    public ReceivedRequestsAdapter(List<Request> requests, String currentUser, Context context) {
        this.requests = requests;
        this.currentUser = currentUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Request request = requests.get(position);
        holder.authorView.setText(request.getAuthor());
        String rate = String.format("%s € ➡ %s €", request.getCurrentHourRate(), request.getDesiredHourRate());
        holder.rateView.setText(rate);
        holder.dateView.setText(request.getDate());

        if (!isRequestAnsweredByCurrentUser(request)) {
            holder.colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.color_indicator_no));
            holder.addAnswerBtn.setVisibility(View.VISIBLE);
            holder.viewAnswerBtn.setVisibility(View.GONE);
        } else {
            holder.colorIndicator.setBackgroundColor(ContextCompat.getColor(context, R.color.color_indicator_all));
            holder.addAnswerBtn.setVisibility(View.GONE);
            holder.viewAnswerBtn.setVisibility(View.VISIBLE);
        }
        holder.viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity) context).showAlertDialogNow(getAnswerOfCurrentUser(request), context.getString(R.string.fragment_received_requests_view_answer_title));
            }
        });
        holder.addAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity) context).showAddAnswerDialog(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    private boolean isRequestAnsweredByCurrentUser(Request request) {
        for (Answer answer : request.getAnswers()) {
            if (answer.getAuthor().equals(currentUser)) {
                return true;
            }
        }
        return false;
    }

    private String getAnswerOfCurrentUser(Request request) {
        for (Answer answer : request.getAnswers()) {
            if (answer.getAuthor().equals(currentUser)) {
                return answer.getContent();
            }
        }
        return "";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView authorView;
        private TextView rateView;
        private TextView dateView;
        private TextView viewAnswerBtn;
        private TextView addAnswerBtn;
        private View colorIndicator;

        public ViewHolder(View view) {
            super(view);
            authorView = (TextView) view.findViewById(R.id.author_view);
            rateView = (TextView) view.findViewById(R.id.rate_view);
            dateView = (TextView) view.findViewById(R.id.date_view);
            viewAnswerBtn = (TextView) view.findViewById(R.id.view_your_answer_btn);
            addAnswerBtn = (TextView) view.findViewById(R.id.add_answer_btn);
            colorIndicator = view.findViewById(R.id.color_indicator);
        }
    }
}
