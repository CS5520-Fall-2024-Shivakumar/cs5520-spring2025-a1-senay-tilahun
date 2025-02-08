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

    // method to display the About me toast
    public void showToast(View view) {
        String name = getString(R.string.name);
        String email = getString(R.string.email);
        String aboutMeMsg = getString(R.string.about_me_toast_message, name, email);

        Toast.makeText(this, aboutMeMsg, Toast.LENGTH_SHORT).show();
    }

    // method to handle quic calc button click
    public void openQuicCalc(View view) {
        Intent intent = new Intent(this, QuicCalcActivity.class);
        startActivity(intent);
    }
}