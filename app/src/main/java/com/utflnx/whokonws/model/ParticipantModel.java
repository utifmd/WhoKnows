package com.utflnx.whokonws.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class ParticipantModel implements Serializable {

    private String participantId;
    private String roomId;
    private String userId;
    private int totalQuiz;
    private int totalTime;
    private int currentPage;
    private int timeRemaining;
    private int expired;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

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

    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public boolean isExpired(){
        return getExpired() != 0;
    }

    public void setExpire(boolean expire){
        if (expire)
            this.setExpired(1);
        else
            this.setExpired(0);
    }

    @Override
    public String toString() {
        return "ParticipantModel{" +
                "participantId='" + participantId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", totalQuiz=" + totalQuiz +
                ", totalTime=" + totalTime +
                ", currentPage=" + currentPage +
                ", timeRemaining=" + timeRemaining +
                ", expired=" + expired +
                '}';
    }
}