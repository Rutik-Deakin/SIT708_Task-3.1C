package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calculator extends AppCompatActivity {

    private EditText num1, num2;
    private Button additionBtn, subtractionBtn;
    private TextView result1, result2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        num1 = findViewById(R.id.num1EditText);
        num2 = findViewById(R.id.num2EditText);
        additionBtn = findViewById(R.id.additionBtn);
        subtractionBtn = findViewById(R.id.subtractBtn);
        result1 = findViewById(R.id.result1);
        result2 = findViewById(R.id.result2);


        additionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNum1 = num1.getText().toString().trim();
                String strNum2 = num2.getText().toString().trim();
                if(strNum1.equals("") || strNum2.equals("")){
                    Toast.makeText(Calculator.this, "Please provide input/s", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    float num1Value = Float.parseFloat(strNum1);
                    float num2Value = Float.parseFloat(strNum2);
                    result1.setText(num1Value + " + " +num2Value + " = " + (num1Value + num2Value));
                } catch (NumberFormatException e) {
                    Toast.makeText(Calculator.this, "Invalid input. Please provide valid numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        subtractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNum1 = num1.getText().toString().trim();
                String strNum2 = num2.getText().toString().trim();
                if(strNum1.equals("") || strNum2.equals("")){
                    Toast.makeText(Calculator.this, "Please provide input/s", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    float num1Value = Float.parseFloat(strNum1);
                    float num2Value = Float.parseFloat(strNum2);
                    result1.setText(num1Value + " - " +num2Value + " = " + (num1Value - num2Value));
                    result2.setText(num2Value + " - " +num1Value + " = " + (num2Value - num1Value));

                } catch (NumberFormatException e) {
                    Toast.makeText(Calculator.this, "Invalid input. Please provide valid numbers", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}