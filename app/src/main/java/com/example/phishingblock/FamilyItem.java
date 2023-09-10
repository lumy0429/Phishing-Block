package com.example.phishingblock;

public class FamilyItem {
    String name;
    String number;

    public FamilyItem(String name, String number) { //recyclerview 사용 위한 초기값

        this.name= name;
        this.number=number;
    }



    public String getName() {
        return name;
    }



    public String getNumber(){
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setNumber(String number) {
        this.number = number;



    }
}


