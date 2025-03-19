package edu.northeastern.numad25sp_senaytilahun;

import android.os.Bundle;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FindPrimeActivity extends AppCompatActivity {

     TextView textCurrentNumber, textLatestPrime;
     Button buttonFindPrimes, buttonTerminateSearch;
     CheckBox checkboxPacifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_prime);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init UI elements
        initUI();

        // TODO: Set onClickListeners in the next step.
    }

    void initUI() {
        textCurrentNumber = findViewById(R.id.text_current_number);
        textLatestPrime = findViewById(R.id.text_latest_prime);
        buttonFindPrimes = findViewById(R.id.button_find_primes);
        buttonTerminateSearch = findViewById(R.id.button_terminate_search);
        checkboxPacifier = findViewById(R.id.checkbox_pacifier);
    }
}