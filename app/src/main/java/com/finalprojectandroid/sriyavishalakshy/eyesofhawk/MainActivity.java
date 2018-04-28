package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class is the home page of the app
 * where the food notifications and posts are
 * displayed as per the fragments
 * @author sriyavishalakshy
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
   private FirebaseFirestore firebaseFirestore;

    private String current_user_id;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mainbottomNav;

    private HomeFragment homeFragment;
    private NotifFragment notificationFragment;

    /**
     *This method initializes the home fragment,notification fragments
     * and subscribes the user to pushNotifications on food alert
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");


        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        //setting the app title
        getSupportActionBar().setTitle("Eyes Of Hawk");
        initializeFragment();
        mainbottomNav=(BottomNavigationView)findViewById(R.id.mainBottomNav);
        if(mAuth.getCurrentUser() != null) {



            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (item.getItemId()) {

                        case R.id.bottom_action_home:
                            //To show the home fragment with general events
                            replaceFragment(homeFragment, currentFragment);
                            return true;


                        case R.id.bottom_action_notif:
                            //Used to show the events that have left over food serving
                            replaceFragment(notificationFragment, currentFragment);
                            return true;

                        default:
                            return false;


                    }

                }
            });
            //listener for the action button to add the new post
            addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //forwarding the user to the page to post a new event
                    Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                    startActivity(newPostIntent);

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendToLogin();

        }

    }
    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

    /**
     *create menu option to logout of the app
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_logout_btn:
                mAuth.signOut();
                sendToLogin();
         return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *This is used to initilize the fragments of
     * home page and food notification fragments
     *
     */

    private void initializeFragment(){

        // FRAGMENTS
        homeFragment = new HomeFragment();
        notificationFragment = new NotifFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, notificationFragment);
        //we are initializing only the home fragment on start and hiding the notification fragment
        fragmentTransaction.hide(notificationFragment);

        fragmentTransaction.commit();

    }

    /**
     *This is used to hide a fragment and show another on user touch
     * @param fragment
     * @param currentFragment
     */
    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == homeFragment){

            fragmentTransaction.hide(notificationFragment);

        }



        if(fragment == notificationFragment){

            fragmentTransaction.hide(homeFragment);

        }
        fragmentTransaction.show(fragment);

        fragmentTransaction.commit();

    }

}
