package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


/**
 *This Activity for adding a new post in android
 *@author sriyavishalakshy
 */
public class NewPostActivity extends AppCompatActivity {
    private Toolbar newPostToolbar;

    private EditText newPostRoomNo;
    private EditText newPostDesc;
    private EditText newPostEvent;
    private Button newPostBtn;

    private Uri postImageUri = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;
    private Boolean switchState = false;



    /**
     *Used to fetch the input for new post and insert into the database depending on
     * the type of post that has been made
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getEmail();

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostRoomNo = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostBtn = findViewById(R.id.post_btn);
        newPostEvent = findViewById(R.id.new_post_event);
        newPostProgress = findViewById(R.id.new_post_progress);
        Switch simpleSwitch = (Switch) findViewById(R.id.isFood);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled to send push notification as food is available
                    switchState = true;
                } else {
                    // The toggle is disabled to send push notification if its a general event post
                    switchState = false;
                }
            }
        });


        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDesc.getText().toString();
                final String roomNo = newPostRoomNo.getText().toString();
                final String eventInfo = newPostEvent.getText().toString();

                if(!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(roomNo) &&!TextUtils.isEmpty(current_user_id )){
                    Map<String, Object> postMap = new HashMap<>();

                    postMap.put("room_no", roomNo);
                    postMap.put("desc", desc);
                    postMap.put("event_info", eventInfo);
                    postMap.put("user_id", current_user_id);
                    postMap.put("timestamp", FieldValue.serverTimestamp());
                    addToDatabase(postMap,switchState);


                                    }


                            else {

                                Toast.makeText(NewPostActivity.this, "Errorss", Toast.LENGTH_LONG).show();

                            }

                        }
                    });


                }

    /**
     * This method is used to fetch parameters and enter it into the database
     * if the switch of it being a food notification was pushed then it will insert into the food database
     * @param postMap
     * @param switchState
     */
                void addToDatabase(Map<String, Object> postMap, Boolean switchState)
                {
                    String dbToAddPost=null;
                    if(switchState) {

                        dbToAddPost= "Food";

                    }
                    else
                    {
                        dbToAddPost = "Posts";

                    }
                    firebaseFirestore.collection(dbToAddPost).add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){

                                movetoHomeScreen();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                showErrorMessage(errorMessage);

                            }

                            newPostProgress.setVisibility(View.INVISIBLE);

                        }
                    });
                }

    /**
     * On a successful insert the user has to move to the home screen
     */
    private void movetoHomeScreen() {
        Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    /**
     * In case of any errors the appropriate error string has to be shown
     * @param errorMessage
     */
    private void showErrorMessage(String errorMessage) {
        Toast.makeText(NewPostActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

    }


}


