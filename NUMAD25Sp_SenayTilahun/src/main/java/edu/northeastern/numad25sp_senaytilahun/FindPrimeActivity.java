package edu.northeastern.numad25sp_senaytilahun;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FindPrimeActivity extends AppCompatActivity {

    TextView textCur, textLatest;
    Button buttonFind, buttonStop;
    CheckBox checkboxPacifier;

    // instance variables for finding primes
    boolean isSearching = false;
    Thread primeThread;
    int latest = 2;
    int cur = 3;

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

        // Set onClickListeners
        buttonFind.setOnClickListener(v -> beginSearch());
        buttonStop.setOnClickListener(v -> terminateSearch());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isSearching) {
                    new AlertDialog.Builder(FindPrimeActivity.this)
                            .setTitle("Exit Search")
                            .setMessage("Sure you want to stop search?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                terminateSearch();
                                finish();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();
                } else {
                    finish(); // Close the activity normally
                }
            }
        });
    }
    void beginSearch() {
        if (isSearching) return;

        isSearching = true;
        cur = 3;
        primeThread = new Thread(() -> {
            while (isSearching) {
                final int currentNumber = cur;
                long startTime = System.currentTimeMillis();

                runOnUiThread(() -> {
                    textCur.setText("Checking: " + currentNumber);
                });

                if (isPrimeNumber(currentNumber)) {
                    latest = currentNumber;
                    runOnUiThread(() -> {
                        textLatest.setText("Latest Prime: " + latest);
                    });
                }

                cur += 2;
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                Log.d("PrimeThread", "Checked: " + currentNumber + ", Time: " + elapsedTime + "ms");
            }
        });
        primeThread.start();
    }

    void terminateSearch() {
        // update our flag
        isSearching = false;
        // kill out workrer thread, if its is runinig
        if (primeThread != null) {
            primeThread.interrupt();
            primeThread = null;
        }
    }

    boolean isPrimeNumber(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;

        for (int i = 3; i * i <= num; i+= 2) {
            if (num % i == 0) return false;
        }

        return true;
    }

    void initUI() {
        textCur = findViewById(R.id.text_current_number);
        textLatest = findViewById(R.id.text_latest_prime);
        buttonFind = findViewById(R.id.button_find_primes);
        buttonStop = findViewById(R.id.button_terminate_search);
        checkboxPacifier = findViewById(R.id.checkbox_pacifier);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSearching", isSearching);
        outState.putInt("latest", latest);
        outState.putInt("cur", cur);
        outState.putBoolean("pacifierChecked", checkboxPacifier.isChecked());
    }

}