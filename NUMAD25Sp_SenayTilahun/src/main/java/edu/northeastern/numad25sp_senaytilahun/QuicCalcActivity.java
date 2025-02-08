package edu.northeastern.numad25sp_senaytilahun;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuicCalcActivity extends AppCompatActivity {

    private TextView calcTextView;
    private StringBuilder current = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quic_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // find our text: CALC
        calcTextView = findViewById(R.id.text_calc);

        // find the numbers
        for (int id : getNumberButtonIds()) {
            Button button = findViewById(id);
            button.setOnClickListener(this::onNumberClick);
        }

        // find the operators by id and set the onClick listeners
        findViewById(R.id.button_plus).setOnClickListener(this::onOperatorClick);
        findViewById(R.id.button_minus).setOnClickListener(this::onOperatorClick);

        // find the x and = and set the onClick listeners
        findViewById(R.id.button_x).setOnClickListener(this::onDeleteClick);
        findViewById(R.id.button_equals).setOnClickListener(this::onEvaluateClick);
    }

    // method that returns the IDs of number buttons
    private int[] getNumberButtonIds() {
        return new int[]{
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9
        };
    }

    // Helper method to update expression and display text
    private void updateExpression(String value) {
        current.append(value);
        calcTextView.setText(current.toString());
    }

    // handle what happens when the numbers are clicked
    private void onNumberClick(View view) {
        // grab the button
        Button button = (Button) view;
        // update the text at the top
        updateExpression(button.getText().toString());

    }

    // handle what happens when the operators are clicked
    private void onOperatorClick(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();

        // Allow '-' at the start of the expression
        if (current.length() == 0 && operator.equals("-")) {
            updateExpression(operator);
            return;
        }

        // edge case - we can't start with an operator
        if (current.length() != 0) {
            // update the text at the top, but make sure to not update two operators next to each other
            char last = current.charAt(current.length() - 1);
            if ((last != '+') && (last != '-')) {
                updateExpression(operator);
            }
        }
    }

    // handle what happens when the evaluate button is clicked
    private void onEvaluateClick(View view) {
        // try to evaluate - if its a valid expression, else catch and throw error
        try {
            int answer = performCalc(current.toString());
            // reset the text to the result
            current.setLength(0);
            updateExpression(String.valueOf(answer));
        } catch (Exception e) {
            calcTextView.setText(R.string.error);
            current.setLength(0);
        }
    }

    private int performCalc(String string) {
        // edge case - if string starts with -
        if (string.startsWith("-")) {
            string = "0" + string;
        }
        // split the string into the different parts to then evaluate a math expression
        String[] split = string.split("(?=[+-])");
        int ans = 0;

        for (String str : split) {
            if (!str.isEmpty()) {
                ans += Integer.parseInt(str);
            }
        }

        return ans;
    }

    // handle what happens when the delete button is clicked
    private void onDeleteClick(View view) {
        // edge case - must have at least one character in the current text displayed
        if (current.length() > 0) {
            current.deleteCharAt(current.length() - 1);

            // if empty - return to default
            if (current.length() == 0) {
                calcTextView.setText(R.string.CALC);
            } else  {
                calcTextView.setText(current.toString());
            }
        }
    }
}
