package com.csc4320.activityTracker;

import java.io.Serializable;

public class ActivityWorkout implements Serializable {
    private String workOutName;
    private int seconds;

    public ActivityWorkout(){
        this.workOutName="";
        this.seconds=0;
    }
    public ActivityWorkout(String workOutName, int seconds){
        this.workOutName=workOutName;
        this.seconds=seconds;
    }

    public void setWorkOutName(String workOutName) {
        this.workOutName = workOutName;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getWorkOutName() {
        return workOutName;
    }

    public int getSeconds() {
        return seconds;
    }
}
