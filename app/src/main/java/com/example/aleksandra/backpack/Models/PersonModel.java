package com.example.aleksandra.backpack.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonModel implements Serializable {
    public String name;
    public String state;
    public String image;
    public List<CommentModel> comment;
    public String points;

    public PersonModel(String name, String state) {
        this.name = name;
        this.state = state;
        this.comment = comment;
    }

    public PersonModel(String name, String state, String image, String points) {
        this.name = name;
        this.state = state;
        this.comment = new ArrayList<>();
        this.image = image;
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setComment(List<CommentModel> comment) {
        this.comment = comment;
    }

    public List<CommentModel> getComment() {

        return comment;
    }

    public void addComment(CommentModel s) {
        comment.add(s);
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
