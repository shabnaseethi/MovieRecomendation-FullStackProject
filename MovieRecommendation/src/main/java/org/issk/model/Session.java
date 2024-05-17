package org.issk.model;

import java.util.Date;

public class Session {

    private int session_id;
    private String username;
    private Date start_time;

    public Session(int session_id) {
        this.session_id = session_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }
}
