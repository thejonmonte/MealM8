package com.example.mealm8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

// TODO: Make work once User class is finished

public class UserSettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button saveSettings, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Get components of the page
        radioGroup = (RadioGroup) findViewById(R.id.profilePicGroup);
        saveSettings = (Button) findViewById(R.id.saveSettings);
        logout = (Button) findViewById(R.id.logout);

        // Get the userID
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        boolean logged = sp.getBoolean("logged", false);
        final int userID = sp.getInt("userID", -1);

        // If user is not logged in, leave page, although hopefully we wouldn't fucking be here
        if (!logged) {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToNextActivity); return;
        }

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // radioGroup on-checked listener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });

        // saveSettings on-clicked listener
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if (radioButtonId != -1) {
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonId);
                    User.updateProfilePic(radioButton.getBackground().toString());
                }
            }
        });

        // logout on-clicked listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(userID);
                Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToNextActivity); return;
            }
        });
    }
}
