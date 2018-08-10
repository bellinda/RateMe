package com.angelova.w510.rateme;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.angelova.w510.rateme.fragments.MyRequestsFragment;
import com.angelova.w510.rateme.fragments.ReceivedRequestsFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mUserEmail = getIntent().getStringExtra("email");

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("email", mUserEmail);
        // set Fragmentclass Arguments
        MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
        myRequestsFragment.setArguments(bundle);
        transaction.replace(R.id.content, myRequestsFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_my_requests:
                    Bundle bundle = new Bundle();
                    bundle.putString("email", mUserEmail);
                    // set Fragmentclass Arguments
                    MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
                    myRequestsFragment.setArguments(bundle);
                    transaction.replace(R.id.content, myRequestsFragment).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_received_requests:
                    Bundle bundleA = new Bundle();
                    ReceivedRequestsFragment receivedRequestsFragment = new ReceivedRequestsFragment();
                    receivedRequestsFragment.setArguments(bundleA);
                    transaction.replace(R.id.content, receivedRequestsFragment).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
            }
            return false;
        }

    };
}
