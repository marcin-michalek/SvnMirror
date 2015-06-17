package com.svn.utils.mirror.app;

import java.util.Date;

/**
 * Created by lukasgol on 17.06.15.
 */
public class Revision {

    long id;
    String author;
    Date date;
    String message;
    String changes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        String info="";
        info+="\n---------------------------------------------\n";
        info+="\nrevision: " + getId();
        info+="\nauthor: " + getAuthor();
        info+="\ndate: " + getDate();
        info+="\nlog message: " + getMessage();
        info+="\nchanged paths:" + getChanges();
        return info;
    }
}
