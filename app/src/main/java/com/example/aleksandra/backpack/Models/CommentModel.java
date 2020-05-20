package com.example.aleksandra.backpack.Models;

import java.io.Serializable;

public class CommentModel implements Serializable {
    private String comment;
    private String title;
    private String details;
    private String timePassed;
    private String idUser;
    private String idPlace;
    public  String commentKey;
    public CommentModel(String comment, String title, String details, String timePassed) {
        this.comment = comment;
        this.title = title;
        this.details = details;
        this.timePassed = timePassed;

    }
    public  CommentModel()
    {

    }
    public  CommentModel(String comment, String idUser,String idPlace)
    {
        this.comment=comment;
        this.idUser=idUser;
        this.idPlace=idPlace;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(String timePassed) {
        this.timePassed = timePassed;
    }

}
