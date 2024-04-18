package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    private String userName;
    private Button option1Button, option2Button, option3Button, option4Button,selectedButton, submitButton;
    private LinearLayout optionsLayout;
    private List<Question> questionList;
    ProgressBar progressBar;
    private TextView questionTextView, questionProgressTextView;
    private int currentQuestionIndex = 0;
    private String userAnswer = "";
    private int totalQuestions = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        progressBar = findViewById(R.id.progressBar);
        questionProgressTextView = findViewById(R.id.questionProgressTextView);
        questionTextView = findViewById(R.id.questionTextView);
        optionsLayout = findViewById(R.id.optionsLayout);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        submitButton = findViewById(R.id.submitButton);

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        userName = sharedPreferences.getString("USERNAME", "");

        // Set username as app title
        setTitle(("Welcome " + userName));

        // Load questions from JSON file
        loadQuestionsFromJson();

        // Assuming totalQuestions is the total number of questions in the quiz
        totalQuestions = questionList.size();

        updateProgress(currentQuestionIndex);

        // Handling option selection
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                selectedButton = clickedButton;
                // Get the text of the clicked button
                String buttonText = clickedButton.getText().toString();
                userAnswer = buttonText;
                // Set background color of clicked button
                clickedButton.setBackgroundColor(getResources().getColor(R.color.selectedOption));
                // Loop through all option buttons to set background color
                for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                    View optionView = optionsLayout.getChildAt(i);
                    if (optionView instanceof Button) {
                        Button optionButton = (Button) optionView;
                        // Check if the button is not the clicked button
                        if (optionButton != clickedButton) {
                            changeButtonColor(optionButton, "default");
                        }
                    }
                }
            }
        };

        option1Button.setOnClickListener(optionClickListener);
        option2Button.setOnClickListener(optionClickListener);
        option3Button.setOnClickListener(optionClickListener);
        option4Button.setOnClickListener(optionClickListener);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitButton.getText().toString().equals("Submit")) {
                    submitAnswer();
                } else {
                    userAnswer = "";
                    changeButtonColor(option1Button, "default");
                    changeButtonColor(option2Button, "default");
                    changeButtonColor(option3Button, "default");
                    changeButtonColor(option4Button, "default");
                    if ((currentQuestionIndex+1) != totalQuestions) {
                        currentQuestionIndex+=1;
                        setQuestion(questionList.get(currentQuestionIndex));
                        updateProgress(currentQuestionIndex);
                    } else {
                        Intent intent = new Intent(QuizQuestions.this, EndQuiz.class);
                        intent.putExtra("totalQuestions", totalQuestions);
                        intent.putExtra("score", score);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    private void submitAnswer() {
        Log.d("in method", currentQuestionIndex+"");
        String correctAnswer = questionList.get(currentQuestionIndex).getCorrectAnswer();
        if (userAnswer.trim().isEmpty()) {
            Toast.makeText(QuizQuestions.this, "Please choose an option", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userAnswer.equals(correctAnswer)) {
            changeButtonColor(selectedButton, "green");
            score+=1;
        } else {
            changeButtonColor(selectedButton, "red");
            // Find and change color of the correct answer button
            if (correctAnswer.equals(option1Button.getText().toString())) {
                changeButtonColor(option1Button, "green");
            } else if (correctAnswer.equals(option2Button.getText().toString())) {
                changeButtonColor(option2Button, "green");
            } else if (correctAnswer.equals(option3Button.getText().toString())) {
                changeButtonColor(option3Button, "green");
            } else if (correctAnswer.equals(option4Button.getText().toString())) {
                changeButtonColor(option4Button, "green");
            }
        }
        submitButton.setText("Next!");
    }

    private void loadQuestionsFromJson() {
        questionList = new ArrayList<>();
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject rootObject = new JSONObject(json);
            JSONArray jsonArray = rootObject.getJSONArray("questions");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String questionText = jsonObject.getString("question");
                String correctAnswer = jsonObject.getString("correct_answer");
                JSONArray optionsArray = jsonObject.getJSONArray("options");
                String[] options = new String[optionsArray.length()];
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }
                Question question = new Question(questionText, options, correctAnswer);
                questionList.add(question);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        setQuestion(questionList.get(0));
    }
    private void updateProgress(int index) {
        questionProgressTextView.setText(((index+1)+"/"+totalQuestions));
        progressBar.setProgress(((index+1)*100)/totalQuestions);
    }
    private void setQuestion(Question question) {
        questionTextView.setText(question.getQuestionText());
        option1Button.setText(question.getOptions()[0]);
        option2Button.setText(question.getOptions()[1]);
        option3Button.setText(question.getOptions()[2]);
        option4Button.setText(question.getOptions()[3]);

        submitButton.setText("Submit");
    }
    private void changeButtonColor(Button btn, String color) {
        int colorResId;
        switch (color) {
            case "green":
                colorResId = R.color.green;
                break;
            case "red":
                colorResId = R.color.red;
                break;
            case "selectedOption":
                colorResId = R.color.selectedOption;
                break;
            default:
                colorResId = R.color.defaultOptionColor;
                break;
        }
        btn.setBackgroundColor(getResources().getColor(colorResId));
    }

}
