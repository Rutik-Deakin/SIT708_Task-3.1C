package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndQuiz extends AppCompatActivity {

    private String userName;
    private TextView congratsText, scoreTextView;
    Button retakeQuiz, finishQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_quiz);

        congratsText = findViewById(R.id.congratsTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        retakeQuiz = findViewById(R.id.retakeQuiz);
        finishQuiz = findViewById(R.id.finishQuiz);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        userName = sharedPreferences.getString("USERNAME", "");

        congratsText.setText("Congratulations ".concat(userName.trim()+"!"));

        Intent intent = getIntent();
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);
        int score = intent.getIntExtra("score", 0);

        scoreTextView.setText(score+"/"+totalQuestions);

        retakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizQuestionIntent = new Intent(EndQuiz.this, QuizQuestions.class);
                startActivity(quizQuestionIntent);
            }
        });

        finishQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close all activities and exit the app
                finishAffinity();
                System.exit(0);
            }
        });
    }
}