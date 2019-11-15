package com.example.mealm8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSearchPage(View view) {
        // Create intent to send to search results activity page when Search button is pressed
        Intent intent = new Intent(this, SearchPageActivity.class);
        startActivity(intent);
    }
}
