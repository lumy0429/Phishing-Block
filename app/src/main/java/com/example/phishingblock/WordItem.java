package com.example.phishingblock;

public class WordItem {

    String words;

    public WordItem(String words) { // 초깃값
        this.words=words;
    }
    public String getWords(){
        return words;
    }

    public void setWords(String words) {this.words= words;}
}
