package com.example.mealm8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchPageActivity extends AppCompatActivity {
    ArrayList<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        ingredients = new ArrayList<String>();
    }

    public void addIngredient(View view) {
        // Get input string from the text box
        EditText searchInput = (EditText) findViewById(R.id.searchInput);
        String currentIngredient = searchInput.getText().toString().trim();

        // Clear the text in the text box
        searchInput.getText().clear();

        // Add ingredient to ingredients list
        if (currentIngredient != null) {
            ingredients.add(currentIngredient);
            TextView inputtedTextView = (TextView) findViewById(R.id.inputtedIngredients);
            String inputtedText = inputtedTextView.getText().toString();

            // Add ingredient to string
            inputtedText += currentIngredient + " ";
            inputtedTextView.setText(inputtedText);
        }
    }

    public void findRecipes(View view) {
        // Create intent to send to search results activity page when Search button is pressed
        Intent resultsIntent = new Intent(this, SearchResultsActivity.class);
        resultsIntent.putExtra("ingredients", ingredients);
        startActivity(resultsIntent);
    }
}
