package com.example.telemedicine.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedicine.Fragments.FragmentHandler;
import com.example.telemedicine.Models.UserConsult;
import com.example.telemedicine.R;
import com.example.telemedicine.Utils.Storage;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView toolbarTitle;
    private ImageView backButton;
    private FloatingActionButton addButton;
    private BottomNavigationView navigation;

    private FragmentHandler fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Fragment Handler
        fragmentHandler = new FragmentHandler(this);

        navigation = findViewById(R.id.navigation_menu);
        navigation.setOnNavigationItemSelectedListener(this);

        toolbarTitle = findViewById(R.id.toolbar_title);
        backButton = findViewById(R.id.back_button);

        addButton = findViewById(R.id.main_add);
        addButton.setOnClickListener(view -> {
            checkIfNotExpiredToken();
            HomeAction();
            navigation.setSelectedItemId(R.id.action_home);
            fragmentHandler.loadHomeFragment();
        });

        backButton.setOnClickListener(view -> {
            checkIfNotExpiredToken();
            fragmentBack();
        });

        HomeAction();
        fragmentHandler.loadHomeFragment();
    }

    public void openDoctorList(View view) {
        checkIfNotExpiredToken();
        DoctorListAction();
        fragmentHandler.loadDocListFragment();
    }

    public void openHomeFragment() {
        checkIfNotExpiredToken();
        navigation.setSelectedItemId(R.id.action_home);
        HomeAction();
        fragmentHandler.loadHomeFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        checkIfNotExpiredToken();
        switch (item.getItemId()) {
            case R.id.action_home:
                HomeAction();
                return fragmentHandler.loadHomeFragment();
            case R.id.action_notifications:
                NotificationAction();
                return fragmentHandler.loadNotificationFragment();
            case R.id.action_schedule:
                break;
            case R.id.action_profile:
                ProfileAction();
                return fragmentHandler.loadProfileFragment();
        }
        return false;
    }

    public void setToolbarTitle(String name) {
        toolbarTitle.setText(name);
    }

    public void setNotificationBadge(boolean isActive) {
        if (isActive) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.action_notifications);
            badge.setBackgroundColor(Color.parseColor("#ff2347"));
        } else {
            if (navigation.getBadge(R.id.action_notifications) != null) {
                navigation.removeBadge(R.id.action_notifications);
            }
        }
    }

    private boolean getAnyNotification() {
        try {
            Storage storage = new Storage(this);
            UserConsult consult = storage.getLastUnconfirmedConsult();
            if (consult == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void fragmentBack() {
        switch (fragmentHandler.getCurrentFragmentName()) {
            case "NotificationFragment":
            case "DoctorListFragment":
            case "ProfileFragment":
                HomeAction();
                navigation.setSelectedItemId(R.id.action_home);
                fragmentHandler.loadHomeFragment();
                break;
            case "DoctorDetailsFragment":
                DoctorListAction();
                fragmentHandler.loadDocListFragment();
                break;
        }
    }

    private void HomeAction() {
        setToolbarTitle("Home");
        if (getAnyNotification()) {
            setNotificationBadge(true);
        } else {
            setNotificationBadge(false);
        }
        backButton.setVisibility(View.GONE);
    }

    private void NotificationAction() {
        setToolbarTitle("Notification");
        backButton.setVisibility(View.VISIBLE);
    }

    private void ProfileAction() {
        setToolbarTitle("Profile");
        backButton.setVisibility(View.VISIBLE);
    }

    private void DoctorListAction() {
        setToolbarTitle("Doctor List");
        backButton.setVisibility(View.VISIBLE);
        navigation.setSelectedItemId(R.id.action_blank);
    }

    private void checkIfNotExpiredToken() {
        try {
            Storage storage = new Storage(this);
            if (!storage.validateToken()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Login session has ended. Please enter credentials again!");
                builder.setCancelable(false);

                builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                    logoutIntent();
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logoutIntent() {
        final Intent intent = new Intent(MainActivity.this, WelcomeScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
