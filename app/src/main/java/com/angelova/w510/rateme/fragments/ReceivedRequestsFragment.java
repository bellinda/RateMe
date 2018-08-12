package com.angelova.w510.rateme.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.rateme.R;
import com.angelova.w510.rateme.adapters.OwnRequestsAdapter;
import com.angelova.w510.rateme.adapters.ReceivedRequestsAdapter;
import com.angelova.w510.rateme.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class ReceivedRequestsFragment extends Fragment {

    private TextView mNoItemsView;
    private RecyclerView mRecyclerView;
    private ReceivedRequestsAdapter mAdapter;
    private List<Request> mDataList = new ArrayList<>();

    private FirebaseFirestore mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_received_requests, container, false);

        mDb = FirebaseFirestore.getInstance();

        mNoItemsView = (TextView) rootView.findViewById(R.id.no_items_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        final String author = getArguments().getString("email");

        getAllRequestsWhereCurrentUserIsRecipient(author);

        return rootView;
    }

    private void getAllRequestsWhereCurrentUserIsRecipient(final String userEmail) {
        final List<Request> requests = new ArrayList<>();

        //TODO: change with whereArrayContains when it is available
        mDb.collection("requests")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Request request = document.toObject(Request.class);
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
            }
        });
    }
}
