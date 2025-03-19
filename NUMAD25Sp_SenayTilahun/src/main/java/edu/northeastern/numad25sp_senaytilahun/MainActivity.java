package edu.northeastern.numad25sp_senaytilahun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // method to display the About me activity
    public void openAboutMe(View view) {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    // method to handle quic calc button click
    public void openQuicCalc(View view) {
        Intent intent = new Intent(this, QuicCalcActivity.class);
        startActivity(intent);
    }

    public void openContactsCollector(View view) {
        Intent intent = new Intent(this, ContactsCollectorActivity.class);
        startActivity(intent);
    }

    public void openPrimeSearch(View view) {
        Intent intent = new Intent(this, PrimeSearchActivity.class);
        startActivity(intent);
    }

}