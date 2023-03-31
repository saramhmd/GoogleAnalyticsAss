package com.example.googleanalyticsass;

public class NotesCategory {
    String id;
    String noteName;
    String categId;
    String noteDescription;

//    private  NotesCategory(){}
    NotesCategory(String id, String categoryName) {
        this.id = id;
        this.noteName = categoryName;

    }
    NotesCategory(String id, String categoryName, String noteDescription) {
        this.id = id;
        this.noteName = categoryName;
        this.noteDescription = noteDescription;
    }
    NotesCategory(String id, String categoryName, String noteDescription , String categId ) {
        this.id = id;
        this.noteName = categoryName;
        this.noteDescription = noteDescription;
        this.categId = categId;

    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return noteName;
    }

//    public String getNoteTitle() {
//        return noteTitle;
//    }
public String categId() {
    return categId;
}

    public String getNoteDescription() {
        return noteDescription;
    }

}
