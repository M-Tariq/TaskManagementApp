package com.example.officetaskmanagement.Model;

public class Data {
   private String title;
   private String note;
   private String id;
   private String date;
   public Data(){}

    public Data(String title, String note, String id, String date) {
        this.title = title;
        this.note = note;
        this.id = id;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
