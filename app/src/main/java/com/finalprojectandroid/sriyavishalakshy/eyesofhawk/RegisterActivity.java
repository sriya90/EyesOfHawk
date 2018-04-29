package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This is activity class to register new user and add the user to the database
 * @author sriyavishalakshy
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private ProgressBar reg_progress;
    private FirebaseFirestore firebaseFirestore;

    private FirebaseAuth mAuth;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        reg_email_field = findViewById(R.id.reg_email);
        reg_pass_field = findViewById(R.id.reg_pass);
        reg_confirm_pass_field = findViewById(R.id.reg_confirm_pass);
        reg_btn = findViewById(R.id.reg_btn);
        reg_login_btn = findViewById(R.id.reg_login_btn);
        reg_progress = findViewById(R.id.reg_progress);

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)){
                    if(isHawkEmail(email))
                    {
                        if (pass.equals(confirm_pass)) {

                            reg_progress.setVisibility(View.VISIBLE);

                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        //assign user to current user on successful log in
                                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                        mUser.getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
                                                        }
                                                    }
                                                });

                                        Intent setupIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(setupIntent);
                                        finish();

                                    } else {

                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                    }

                                    reg_progress.setVisibility(View.INVISIBLE);

                                }
                            });

                        } else {

                            Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                        }
                    }else
                    {
                        Toast.makeText(RegisterActivity.this, "Please Use Hawk ID", Toast.LENGTH_LONG).show();


                    }
                }


            }
        });


    }

    /**
     *Login to on start
     */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            sendToMain();

        }

    }

    /**
     * Used to send the user to main page after log in
     */
    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }

    /**
     *To check if user has valid hawk id
     * @param email
     * @return boolean
     */
    private boolean isHawkEmail(String email)
    {
        return email.endsWith("@hawk.iit.edu");
    }
}
