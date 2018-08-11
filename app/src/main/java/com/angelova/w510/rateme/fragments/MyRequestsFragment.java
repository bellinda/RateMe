package com.angelova.w510.rateme.fragments;

import android.content.Intent;
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
import com.angelova.w510.rateme.dialogs.AddRequestDialog;
import com.angelova.w510.rateme.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class MyRequestsFragment extends Fragment {

    private FloatingActionButton mAddRequestBtn;
    private TextView mNoItemsView;
    private RecyclerView mRecyclerView;
    private OwnRequestsAdapter mAdapter;
    private List<Request> mDataList = new ArrayList<>();

    private FirebaseFirestore mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_requests, container, false);

        mDb = FirebaseFirestore.getInstance();

        mAddRequestBtn = (FloatingActionButton) rootView.findViewById(R.id.add_new_request_btn);
        mNoItemsView = (TextView) rootView.findViewById(R.id.no_items_view);

        final String[] emailArr = {"abv@abv.bg", "donkey@gmail.com", "gabito_ang@abv.bg", "andrea@gmail.com", "anika_lopez@gmail.com", "antonio@gmail.com", "gareth@abv.bg"};
        final String author = getArguments().getString("email");

        mAddRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRequestDialog dialog = new AddRequestDialog(getActivity(), emailArr, new AddRequestDialog.DialogClickListener() {
                    @Override
                    public void onSave(Request request) {
                        request.setAuthor(author);
                        createRequest(request);
                    }
                });
                dialog.show();
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

//        mAdapter = new OwnRequestsAdapter(mDataList, getActivity());
//        mRecyclerView.setAdapter(mAdapter);

        getAllDocuments(author);

        return rootView;
    }

    private void createRequest(Request request) {
        mDb.collection("userRequests").document(request.getAuthor()).collection("requests").add(request)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                    }
                });
    }

    private void getAllDocuments(String userEmail) {
        final List<Request> requests = new ArrayList<>();
        mDb.collection("userRequests").document(userEmail).collection("requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request request = document.toObject(Request.class);
                                requests.add(request);
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            if(requests.size() > 0) {
                                mNoItemsView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mDataList.addAll(requests);
                                //mAdapter.notifyDataSetChanged();

                                mAdapter = new OwnRequestsAdapter(mDataList, getActivity());
                                mRecyclerView.setAdapter(mAdapter);
                            } else {
                                mRecyclerView.setVisibility(View.GONE);
                                mNoItemsView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
