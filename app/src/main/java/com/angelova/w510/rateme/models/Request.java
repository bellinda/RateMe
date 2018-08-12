package com.angelova.w510.rateme.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class Request implements Serializable {

    private List<String> recipients;
    private String author;
    private double currentHourRate;
    private double desiredHourRate;
    private String comment;
    private List<Answer> answers;
    private String date;
    @JsonIgnore
    private String dbId;

    public Request() {

    }

    public Request(List<String> recipients, String author, double currentHourRate, double desiredHourRate, String comment, List<Answer> answers, String date) {
        this.recipients = recipients;
        this.author = author;
        this.currentHourRate = currentHourRate;
        this.desiredHourRate = desiredHourRate;
        this.comment = comment;
        this.answers = answers;
        this.date = date;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getCurrentHourRate() {
        return currentHourRate;
    }

    public void setCurrentHourRate(double currentHourRate) {
        this.currentHourRate = currentHourRate;
    }

    public double getDesiredHourRate() {
        return desiredHourRate;
    }

    public void setDesiredHourRate(double desiredHourRate) {
        this.desiredHourRate = desiredHourRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }
}
