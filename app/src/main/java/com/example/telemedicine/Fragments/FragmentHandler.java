package com.example.telemedicine.Fragments;

import androidx.fragment.app.Fragment;

import com.example.telemedicine.R;
import com.example.telemedicine.UI.MainActivity;

public class FragmentHandler {

    private MainActivity mainActivity;
    private String currentFragmentName = null;

    public FragmentHandler(MainActivity activity) {
        mainActivity = activity;
    }

    public String getCurrentFragmentName() {
        return currentFragmentName;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public boolean loadHomeFragment() {
        Fragment fragment = new HomeFragment(this);
        return loadFragment(fragment);
    }

    public boolean loadNotificationFragment() {
        Fragment fragment = new NotificationFragment(this);
        return loadFragment(fragment);
    }

    public boolean loadProfileFragment() {
        Fragment fragment = new ProfileFragment(this);
        return loadFragment(fragment);
    }

    public void loadDocListFragment() {
        Fragment fragment = new DoctorListFragment(this);
        loadFragment(fragment);
    }

    public void loadDocDetailsFragment(int id) {
        Fragment fragment = new DoctorDetailsFragment(id);
        loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        try {
            if (fragment != null) {
                currentFragmentName = fragment.getClass().getSimpleName();

                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
