package com.example.brickbreaker04.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.brickbreaker04.User;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void initializeViews();
    protected abstract void setListeners();
    public static User currentUser;
}
