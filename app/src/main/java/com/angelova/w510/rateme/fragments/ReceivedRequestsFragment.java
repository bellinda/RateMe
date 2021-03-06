package com.angelova.w510.rateme.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.adapters.OwnRequestsAdapter;
import com.angelova.w510.rateme.adapters.ReceivedRequestsAdapter;
import com.angelova.w510.rateme.dialogs.AddAnswerDialog;
import com.angelova.w510.rateme.models.Answer;
import com.angelova.w510.rateme.models.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class ReceivedRequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView mNoItemsView;
    private RecyclerView mRecyclerView;
    private ReceivedRequestsAdapter mAdapter;
    private List<Request> mDataList = new ArrayList<>();
    private String author;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseFirestore mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_received_requests, container, false);

        mDb = FirebaseFirestore.getInstance();

        mNoItemsView = (TextView) rootView.findViewById(R.id.no_items_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        author = getArguments().getString("email");

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                getAllRequestsWhereCurrentUserIsRecipient(author);
            }
        });

        return rootView;
    }

    private void getAllRequestsWhereCurrentUserIsRecipient(final String userEmail) {
        mSwipeRefreshLayout.setRefreshing(true);
        final List<Request> requests = new ArrayList<>();

        //TODO: change with whereArrayContains when it is available
        mDb.collection("requests")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Request request = document.toObject(Request.class);
                    request.setDbId(document.getId());
                    boolean isRecipient = request.getRecipients().contains(userEmail);
                    System.out.println("IS RECIPIENT " + isRecipient);
                    if (isRecipient) {
                        requests.add(request);
                    }
                }
                if(requests.size() > 0) {
                    mNoItemsView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mDataList.addAll(requests);

                    mAdapter = new ReceivedRequestsAdapter(mDataList, userEmail, getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoItemsView.setVisibility(View.VISIBLE);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void showAddAnswerDialog(final Request request) {
        AddAnswerDialog dialog = new AddAnswerDialog(getActivity(), request, author, new AddAnswerDialog.DialogClickListener() {
            @Override
            public void onSave(Answer answer) {
                request.getAnswers().add(answer);
                ObjectMapper m = new ObjectMapper();
                Map<String,Object> requestMap = m.convertValue(request, Map.class);
                requestMap.remove("dbId");

                mDb.collection("requests").document(request.getDbId()).update(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        refreshAllRequests();
                    }
                });
            }
        });
        dialog.show();
    }

    private void refreshAllRequests() {
        final List<Request> requests = new ArrayList<>();

        //TODO: change with whereArrayContains when it is available
        mDb.collection("requests")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Request request = document.toObject(Request.class);
                    request.setDbId(document.getId());
                    boolean isRecipient = request.getRecipients().contains(author);
                    System.out.println("IS RECIPIENT " + isRecipient);
                    if (isRecipient) {
                        requests.add(request);
                    }
                }
                if (requests.size() > 0) {
                    mDataList.clear();
                    mDataList.addAll(requests);
                    mAdapter.notifyDataSetChanged();
                }
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshAllRequests();
    }
}
