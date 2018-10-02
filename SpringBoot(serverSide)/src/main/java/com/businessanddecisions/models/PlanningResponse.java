package com.businessanddecisions.models;

public class PlanningResponse {
    private String eventName, eventDescription,startDate,endDate,startHour,endHour,id;


    public String getEventName(){
        return eventName;
    }

    public String getEventDescription(){
        return eventDescription;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getStartHour(){
        return startHour;
    }

    public String getEndHour(){
        return endHour;
    }

    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}


}
