package com.angelova.w510.rateme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.angelova.w510.rateme.dialogs.WarnDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mUsernameInput;
    private EditText mPasswordInput;
    private EditText mPasswordRepeatInput;
    private Button mRegisterBtn;
    private ProgressBar mLoader;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#c5ff6600"));
        }

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        mUsernameInput = (EditText) findViewById(R.id.input_username);
        mPasswordInput = (EditText) findViewById(R.id.input_password);
        mPasswordRepeatInput = (EditText) findViewById(R.id.input_password_repeat);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mLoader = (ProgressBar) findViewById(R.id.register_loader);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterBtn.setVisibility(View.GONE);
                mLoader.setVisibility(View.VISIBLE);
                if (mUsernameInput.getText() != null && !mUsernameInput.getText().toString().isEmpty()) {
                    if (mPasswordInput.getText() != null && !mPasswordInput.getText().toString().isEmpty()) {
                        if (mPasswordRepeatInput.getText() != null && !mPasswordRepeatInput.getText().toString().isEmpty()) {
                            if (mPasswordInput.getText().toString().equals(mPasswordRepeatInput.getText().toString())) {
                                registerUser(mUsernameInput.getText().toString(), mPasswordInput.getText().toString());
                            } else {
                                showAlertDialogNow(getString(R.string.register_not_matching_passwords), getString(R.string.register_title));
                            }
                        } else {
                            showAlertDialogNow(getString(R.string.register_missing_repeat), getString(R.string.register_title));
                        }
                    } else {
                        showAlertDialogNow(getString(R.string.register_missing_pass), getString(R.string.register_title));
                    }
                } else {
                    showAlertDialogNow(getString(R.string.register_missing_email), getString(R.string.register_title));
                }
            }
        });
    }


    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            showAlertDialogNow(task.getException().getMessage(), getString(R.string.register_title));
                            mLoader.setVisibility(View.GONE);
                            mRegisterBtn.setVisibility(View.VISIBLE);
                        } else {
                            createUser(email);
                        }
                    }
                });
    }

    private void createUser(String email) {
        Map<String, Object> user = new HashMap<>();
        mDb.collection("users").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                        mLoader.setVisibility(View.GONE);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showAlertDialogNow(getString(R.string.register_successful), getString(R.string.register_title), new WarnDialog.DialogClickListener() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                        mLoader.setVisibility(View.GONE);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showAlertDialogNow(e.getMessage(), getString(R.string.register_title));
                    }
                });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                mLoader.setVisibility(View.GONE);
                mRegisterBtn.setVisibility(View.VISIBLE);
            }
        });
        warning.show();
    }

    private void showAlertDialogNow(String message, String title, WarnDialog.DialogClickListener listener) {
        WarnDialog warnDialog = new WarnDialog(this, title, message, listener);
        warnDialog.show();
    }
}
