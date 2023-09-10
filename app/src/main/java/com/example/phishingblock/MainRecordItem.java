package com.example.phishingblock;

import java.util.ArrayList;
import java.util.HashSet;

public class MainRecordItem {
    String num;
    String time;
    String word;
    Integer pos;
    String result11;
    ArrayList<String> bords_list2 = new ArrayList<>();
    public MainRecordItem(){

    }

    public MainRecordItem(String num, String time, String word, ArrayList<String> bords_list,String result11, Integer pos) {

        this.num= num;
        this.time=time;
        this.word=word;
        this.result11=result11;
        this.pos = pos;


        //중복되는 단어 삭제
        HashSet<String> set = new HashSet<>();
        for(int i = 0 ; i < bords_list.size() ; i++){
            set.add(bords_list.get(i));
        }
        ArrayList<String> bords_list2 = new ArrayList<>(set);

        this.bords_list2 = bords_list2;
    }


    public String getNum() {
        return num;
    }
    public String getTime(){
        return time;
    }
    public String getWord(){
        return word;
    }
    public ArrayList<String> getBords_list2() { return bords_list2;}
    public String getResult(){
        return result11;
    }

    public void setNum(String num) {
        this.num = num;
    }
    public void setTime(String time) {this.time= time;}
    public void setWord(String word) {this.word= word;}
    public void setBords_list2(ArrayList<String> bords_list2) {this.bords_list2 = bords_list2;}
    public void setResult(String result11) {this.result11= result11;}

}
