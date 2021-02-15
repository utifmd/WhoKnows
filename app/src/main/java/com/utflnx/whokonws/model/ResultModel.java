package com.utflnx.whokonws.model;

public class ResultModel {
    private String roomId;
    private String userId;
    private String userName;
    private String correctQuiz;
    private String wrongQuiz;
    private String score;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCorrectQuiz() {
        return correctQuiz;
    }

    public void setCorrectQuiz(String correctQuiz) {
        this.correctQuiz = correctQuiz;
    }

    public String getWrongQuiz() {
        return wrongQuiz;
    }

    public void setWrongQuiz(String wrongQuiz) {
        this.wrongQuiz = wrongQuiz;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}