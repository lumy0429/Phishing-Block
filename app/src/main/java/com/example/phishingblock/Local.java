package com.example.phishingblock;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Local {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private String phone;
    private String relation;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
