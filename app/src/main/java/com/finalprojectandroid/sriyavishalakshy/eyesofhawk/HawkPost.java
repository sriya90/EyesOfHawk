package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import java.util.Date;
/*
This is the object class that represents the posts that have been posted
I will be reusing this class to post about Food and for generic posts about events around campus
@author sriyavishalakshy

*/

public class HawkPost {
    private String user_id, desc, room_no,event_info;
    private Date timestamp;
    public HawkPost()
    {

    }

    /**
     * HawkPost Constructor with parameters that are fetched
     * from firebase database
     * @param user_id
     * @param desc
     * @param room_no
     * @param timestamp
     * @param event_info
     */
    public HawkPost(String user_id, String desc, String room_no, Date timestamp,String event_info) {
        this.user_id = user_id;
        this.desc = desc;
        this.room_no = room_no;
        this.event_info=event_info;
        this.timestamp = timestamp;
    }

    public String getEvent_info() {
        return event_info;
    }

    public void setEvent_info(String event_info) {
        this.event_info = event_info;
    }

    public String getUser_id() {

        return user_id;

    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
