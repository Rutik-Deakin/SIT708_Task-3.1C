package com.example.quiz;
public class Question {
    private String questionText;
    private String[] options;
    private String correctAnswerIndex;

    public Question(String questionText, String[] options, String correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswerIndex;
    }
}

