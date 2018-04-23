package com.finalprojectandroid.sriyavishalakshy.eyesofhawk;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class HawkPost {
    private String user_id, desc, room_no;
    private Date timestamp;
    public HawkPost()
    {

    }
    public HawkPost(String user_id, String desc, String room_no, Date timestamp) {
        this.user_id = user_id;
        this.desc = desc;
        this.room_no = room_no;
        this.timestamp = timestamp;
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
