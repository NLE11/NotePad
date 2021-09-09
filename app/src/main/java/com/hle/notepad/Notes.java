package com.hle.notepad;

import androidx.annotation.NonNull;

import java.util.Date;

public class Notes implements Comparable<Notes> {

    private String title;
    private String content;
    private Date lastDate;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
        this.lastDate = new Date();
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(long lastTimeMS) {
        this.lastDate = lastDate;
    }

    @Override
    public int compareTo(Notes o) {
        if (lastDate.before(o.lastDate))
            return -1;
        else if (lastDate.after(o.lastDate))
            return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return "\n" + title + " | " + content + " | " + lastDate;
    }

}
