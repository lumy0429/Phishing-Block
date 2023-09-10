package com.example.phishingblock;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gpt3Response {
    @SerializedName("choices")
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public class Choice {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }
}
